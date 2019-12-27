package com.dna.sourcing.controller;

import ch.qos.logback.classic.Logger;
import com.dna.sourcing.dao.contract.Contract;
import com.dna.sourcing.dao.contract.ContractCompany;
import com.dna.sourcing.model.contract.input.InputWrapper;
import com.dna.sourcing.model.util.Result;
import com.dna.sourcing.service.ContractService;
import com.dna.sourcing.service.dnaid_server.DnaidServerService;
import com.dna.sourcing.service.oauth.JWTService;
import com.dna.sourcing.service.time.bctsp.TspService;
import com.dna.sourcing.service.util.SyncService;
import com.dna.sourcing.service.util.ValidateService;
import com.dna.sourcing.util.exp.ErrorCode;
import com.google.gson.Gson;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/v1/")
@Configuration
public class ContractController {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(ContractController.class);

    //
    private Gson gson = new Gson();

    //
    private TspService tspService;
    private SyncService syncService;
    private ValidateService validateService;

    //
    private JWTService jwtService;
    private ContractService contractService;
    private DnaidServerService dnaidServerService;

    //
    private final KafkaTemplate<String, Object> kafkaTemplate;

    //
    private final String ontSourcingTopicPut;

    @Autowired
    public ContractController(TspService tspService,
                              SyncService syncService,
                              ValidateService validateService,
                              JWTService jwtService,
                              ContractService contractService,
                              DnaidServerService dnaidServerService,
                              KafkaTemplate<String, Object> kafkaTemplate,
                              @Value("${com.dna.sourcing.kafka.topic.put}") String ontSourcingTopicPut) {
        //
        this.tspService = tspService;
        this.syncService = syncService;
        this.validateService = validateService;
        //
        this.jwtService = jwtService;
        this.contractService = contractService;
        this.dnaidServerService = dnaidServerService;
        //
        this.kafkaTemplate = kafkaTemplate;
        //
        this.ontSourcingTopicPut = ontSourcingTopicPut;
    }

    //
    @PostConstruct
    public void postConstructor() {
        //
        logger.info("ContractController PostConstruct start ...");
    }

    //
    @KafkaListener(topics = "#{'${com.dna.sourcing.kafka.topic.put}'}")
    public void kafkaListener01(List<String> list,
                                Acknowledgment ack) {

        //
        logger.info("start fetching {} from mq ...", list.size());
        if (list.size() <= 0)
            return;

        //
        List<Contract> c_list = new ArrayList<>();
        List<Contract> other_list = new ArrayList<>();

        //
        String first = list.get(0);
        Contract first_contract = gson.fromJson(first, Contract.class);
        String first_dnaid = first_contract.getCompanyDnaid();


        //
        for (String message : list) {

            //
            Contract contract = gson.fromJson(message, Contract.class);

            //
            String filehash = contract.getFilehash();
            String company_dnaid = contract.getCompanyDnaid();

            //
            logger.debug("start processing filehash {} from mq ...", filehash);

            //
            try {
                //
                Map<String, Object> map = tspService.getTimeStampMap(filehash);
                //
                long timestamp = (long) map.get("timestamp");
                String timestampSign = map.get("timestampSign").toString();
                //
                contract.setTimestamp(new Date(timestamp));
                contract.setTimestampSign(timestampSign);
                //
                Map<String, String> map2 = contractService.putContract(contract);
                String txhash = map2.get("txhash");
                contract.setTxhash(txhash);
                //
                if (contract.getCompanyDnaid().equals(first_dnaid)) {
                    c_list.add(contract);
                } else {
                    other_list.add(contract);
                }
                // 链同步
                syncService.confirmTx(txhash);
            } catch (Exception e) {
                // logger.error(e.getMessage());
                logger.error("{}",e);
                // TODO
                return;
            }

            //
            logger.debug("finish processing contract {},{}.", company_dnaid, filehash);
        }

        //
        try {
            contractService.saveToLocalBatch(first_dnaid, c_list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        //
        for (Contract c : other_list) {
            try {
                contractService.saveToLocal(c);
            } catch (Exception e) {
                logger.error(e.getMessage());
                return;
            }
        }

        //
        ack.acknowledge();
    }

    @PostMapping("/dnaid/create")
    public ResponseEntity<Result> creatednaid(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("creatednaid");

        //
        Set<String> required = new HashSet<>();
        required.add("username");
        required.add("password");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String username = (String) obj.get("username");
        String password = (String) obj.get("password");

        //
        try {
            Map<String, String> map = contractService.createDnaid(username, password);
            //
            String keystore = map.get("keystore");
            String txhash = map.get("txhash");
            String dnaid = map.get("dnaid");
            //
            rst.setResult(dnaid);
            //
            rst.setErrorAndDesc(ErrorCode.SUCCESSS);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/dnaid/login")
    public ResponseEntity<Result> login(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("creatednaid");

        //
        Set<String> required = new HashSet<>();
        required.add("username");
        required.add("password");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String username = (String) obj.get("username");
        String password = (String) obj.get("password");

        //
        try {
            //
            Map<String, String> map = contractService.login(username, password);
            //
            rst.setResult(map);
            //
            rst.setErrorAndDesc(ErrorCode.SUCCESSS);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/dnaid/token")
    public ResponseEntity<Result> getAccessToken(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getAccessToken");

        //
        Set<String> required = new HashSet<>();
        required.add("user_dnaid");
        required.add("password");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String user_dnaid = (String) obj.get("user_dnaid");
        String password = (String) obj.get("password");

        //
        try {
            //
            contractService.loginByDnaid(user_dnaid, password);

            //
            Map<String, String> map = jwtService.getAccessToken(user_dnaid);
            //
            // String access_token = map.get("access_token");
            // String txhash = map.get("txhash");
            // String dnaid = map.get("dnaid");
            //
            rst.setResult(map);
            //
            rst.setErrorAndDesc(ErrorCode.SUCCESSS);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/attestation/put")
    public ResponseEntity<Result> putAttestation(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("putAttestation");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");
        required.add("user_dnaid");  // 空表示自己上传；不空表示被别人上传
        required.add("filehash");
        required.add("type");
        required.add("metadata");
        required.add("context");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        InputWrapper iw = gson.fromJson(gson.toJson(obj), InputWrapper.class);

        //
        String access_token = iw.getAccessToken();
        String user_dnaid = iw.getUserDnaid();
        String filehash = iw.getFilehash();
        String type = iw.getType();

        //
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("metadata", iw.getMetadata());
        jsonObject.put("context", iw.getContext());
        jsonObject.put("signature", iw.getSignature());
        String detail = jsonObject.toJSONString();

        //
        boolean async = false;
        if (obj.containsKey("async")) {
            async = (boolean) obj.get("async");
        }

        //
        String company_dnaid = "";
        try {
            company_dnaid = jwtService.getContentUser(access_token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        // 查看项目有没有添加定制信息
        // try {
        //     contractService.checkCompany(company_dnaid);
        // } catch (Exception e) {
        //     logger.error(e.getMessage());
        //     rst.setErrorAndDesc(e);
        //     return new ResponseEntity<>(rst, HttpStatus.OK);
        // }

        //
        if (StringUtils.isEmpty(user_dnaid))
            user_dnaid = company_dnaid;

        //
        try {
            //
            Contract contract = new Contract();
            contract.setDnaid(user_dnaid);
            contract.setCompanyDnaid(company_dnaid);
            contract.setFilehash(filehash);
            contract.setDetail(detail);
            contract.setType(type);
            contract.setCreateTime(new Date());
            //
            if (async) {
                //
                kafkaTemplate.send(ontSourcingTopicPut, gson.toJson(contract, Contract.class));
                //
                rst.setResult(true);
                rst.setErrorAndDesc(ErrorCode.SUCCESSS);
                return new ResponseEntity<>(rst, HttpStatus.OK);

            } else {
                //
                Map<String, Object> map = tspService.getTimeStampMap(filehash);
                //
                long timestamp = (long) map.get("timestamp");
                String timestampSign = map.get("timestampSign").toString();
                //
                contract.setTimestamp(new Date(timestamp * 1000L));
                contract.setTimestampSign(timestampSign);
                //
                Map<String, String> map2 = contractService.putContract(contract);
                String txhash = map2.get("txhash");
                contract.setTxhash(txhash);
                //
                contractService.saveToLocal(contract);
                // 链同步
                syncService.confirmTx(txhash);
                //
                Map<String, String> m = new HashMap<>();
                m.put("txhash", txhash);
                //
                rst.setResult(m);
                rst.setErrorAndDesc(ErrorCode.SUCCESSS);
                return new ResponseEntity<>(rst, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/attestations/put")
    public ResponseEntity<Result> putAttestations(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("putAttestations");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");
        required.add("user_dnaid");
        required.add("filelist");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String access_token = (String) obj.get("access_token");
        String user_dnaid = (String) obj.get("user_dnaid");

        //
        boolean async = false;
        if (obj.containsKey("async")) {
            async = (boolean) obj.get("async");
        }

        //
        String company_dnaid = "";
        try {
            company_dnaid = jwtService.getContentUser(access_token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        // 查看项目有没有添加定制信息
        // try {
        //     contractService.checkCompany(company_dnaid);
        // } catch (Exception e) {
        //     logger.error(e.getMessage());
        //     rst.setErrorAndDesc(e);
        //     return new ResponseEntity<>(rst, HttpStatus.OK);
        // }

        //
        if (StringUtils.isEmpty(user_dnaid))
            user_dnaid = company_dnaid;


        // TODO
        ArrayList<Map<String, Object>> filelist = (ArrayList<Map<String, Object>>) obj.get("filelist");

        //
        try {
            //
            List<Contract> contractList = new ArrayList<>();
            //
            List<Map<String, String>> txhashList = new ArrayList<>();
            //
            for (Map<String, Object> item : filelist) {

                //
                InputWrapper iw = gson.fromJson(gson.toJson(item), InputWrapper.class);

                //
                String filehash = iw.getFilehash();
                String type = iw.getType();
                //
                com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                jsonObject.put("metadata", iw.getMetadata());
                jsonObject.put("context", iw.getContext());
                jsonObject.put("signature", iw.getSignature());
                String detail = jsonObject.toJSONString();

                //
                // ArrayList<Object> detailList = (ArrayList<Object>) item.get("detail");
                // String detail = gson.toJson(detailList);
                // System.out.println(detail);

                //
                Contract contract = new Contract();
                contract.setCompanyDnaid(company_dnaid);
                contract.setDnaid(user_dnaid);
                contract.setFilehash(filehash);
                contract.setDetail(detail);
                contract.setType(type);
                contract.setCreateTime(new Date());

                //
                if (async) {
                    //
                    kafkaTemplate.send(ontSourcingTopicPut, gson.toJson(contract, Contract.class));

                } else {
                    try {

                        //
                        Map<String, Object> map = tspService.getTimeStampMap(filehash);
                        //
                        long timestamp = (long) map.get("timestamp");
                        String timestampSign = map.get("timestampSign").toString();
                        //
                        contract.setTimestamp(new Date(timestamp));
                        contract.setTimestampSign(timestampSign);
                        //
                        Map<String, String> map2 = contractService.putContract(contract);
                        String txhash = map2.get("txhash");
                        contract.setTxhash(txhash);
                        // 链同步
                        syncService.confirmTx(txhash);
                        //
                        contractList.add(contract);
                        //
                        Map<String, String> m = new HashMap<>();
                        m.put("txhash", txhash);
                        txhashList.add(m);

                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        rst.setErrorAndDesc(e);
                        return new ResponseEntity<>(rst, HttpStatus.OK);
                    }
                }
            }

            if (async) {

                //
                rst.setResult(true);
                rst.setErrorAndDesc(ErrorCode.SUCCESSS);
                return new ResponseEntity<>(rst, HttpStatus.OK);

            } else {
                // mybatis batch insert
                // 单条长度大概 4KB，30条，一个sql语句size大概为120KB
                // TODO 检查 max_allowed_packet
                contractService.saveToLocalBatch(company_dnaid, contractList);
                //
                rst.setResult(txhashList);
                rst.setErrorAndDesc(ErrorCode.SUCCESSS);
                return new ResponseEntity<>(rst, HttpStatus.OK);
            }


        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }


    @PostMapping("/attestation/hash")
    public ResponseEntity<Result> selectByDnaidAndHash(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("selectByDnaidAndHash");

        //
        Set<String> required = new HashSet<>();
        required.add("hash");
        required.add("access_token");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String hash = (String) obj.get("hash");
        String accessToken = (String) obj.get("access_token");

        //
        String dnaid = "";
        List<Contract> list = new ArrayList<>();

        //
        try {
            dnaid = jwtService.getContentUser(accessToken);

            // dnaid 也要作为条件，否则查到别人的了
            list = contractService.selectByDnaidAndHash(dnaid, hash);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }

    @PostMapping("/attestation/hash/delete")
    public ResponseEntity<Result> deleteByDnaidAndHash(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("deleteByDnaidAndHash");

        //
        Set<String> required = new HashSet<>();
        required.add("hash");
        required.add("access_token");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String txhash = (String) obj.get("hash");
        String accessToken = (String) obj.get("access_token");

        //
        String dnaid = "";
        try {
            dnaid = jwtService.getContentUser(accessToken);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        // dnaid 也要作为条件
        try {
            //
            List<Contract> exists = contractService.selectByDnaidAndTxHash(dnaid, txhash);
            if (exists.size() != 0) {

                //
                int rt = contractService.updateStatusByDnaidAndHash(dnaid, txhash);
                // System.out.println(rt);

                //
                Contract exist = exists.get(0);
                String c_key = "revoke" + contractService.contractToDigestForKey(exist);
                String c_value = "";
                Map<String, String> map2 = contractService.putContract(exist, c_key, c_value);
                String revokeTx = map2.get("txhash");

                //
                contractService.updateRevokeTx(txhash, revokeTx);

                // todo 保证一定从链上取到
                syncService.confirmTx(revokeTx);

            } else {
                //
                rst.setResult(true);
                rst.setErrorAndDesc(ErrorCode.TXHASH_NOT_EXIST);
                return new ResponseEntity<>(rst, HttpStatus.OK);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        rst.setResult(true);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }

    @PostMapping("/attestation/count")
    public ResponseEntity<Result> count(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("count");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String accessToken = (String) obj.get("access_token");

        //
        String dnaid = "";
        Integer count;
        try {
            dnaid = jwtService.getContentUser(accessToken);
            count = contractService.count(dnaid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        rst.setResult(count);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }


    @PostMapping("/attestation/history")
    public ResponseEntity<Result> getHistory(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getHistory");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");
        required.add("pageNum");
        required.add("pageSize");
        if (obj.containsKey("type")) {
            required.add("type");
        }

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String accessToken = (String) obj.get("access_token");
        int pageNum = Integer.parseInt(obj.get("pageNum").toString());
        int pageSize = Integer.parseInt(obj.get("pageSize").toString());
        //
        String type = "";
        if (obj.containsKey("type")) {
            type = (String) obj.get("type");
        }

        //
        String dnaid = "";
        List<Contract> list = new ArrayList<>();
        try {
            //
            dnaid = jwtService.getContentUser(accessToken);
            //
            list = contractService.getHistoryByDnaid(dnaid, pageNum, pageSize, type);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }


    @PostMapping("/attestation/explorer")
    public ResponseEntity<Result> getExplorer(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getExplorer");

        //
        Set<String> required = new HashSet<>();
        required.add("pageNum");
        required.add("pageSize");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        int pageNum = Integer.parseInt(obj.get("pageNum").toString());
        int pageSize = Integer.parseInt(obj.get("pageSize").toString());

        //
        List<Contract> list = contractService.getExplorerHistory(pageNum, pageSize);
        //
        rst.setResult(list);
        //
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/attestation/explorer/hash")
    public ResponseEntity<Result> getExplorerHash(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getExplorerHash");

        //
        Set<String> required = new HashSet<>();
        required.add("hash");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String hash = (String) obj.get("hash");

        // dnaid 也要作为条件，否则查到别人的了
        List<Contract> list = contractService.selectByHash(hash);

        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }


    @PostMapping("/attestation/company/add")
    public ResponseEntity<Result> addCompany(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("addCompany");

        //
        Set<String> required = new HashSet<>();
        required.add("dnaid");
        required.add("prikey");
        required.add("code_addr");
        //
        required.add("ont_password");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String dnaid = (String) obj.get("dnaid");
        String prikey = (String) obj.get("prikey");
        String code_addr = (String) obj.get("code_addr");

        //
        ContractCompany existed = contractService.getCompany(dnaid);
        if (existed != null) {
            rst.setErrorAndDesc(ErrorCode.DNAID_EXIST);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        ContractCompany company = new ContractCompany();
        company.setDnaid(dnaid);
        company.setPrikey(prikey);
        company.setCodeAddr(code_addr);
        company.setCreateTime(new Date());
        contractService.saveCompany(company);

        //
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }


    @PostMapping("/attestation/company/update")
    public ResponseEntity<Result> updateCompany(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("updateCompany");

        //
        Set<String> required = new HashSet<>();
        required.add("dnaid");
        //
        required.add("ont_password");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String dnaid = (String) obj.get("dnaid");

        //
        String prikey = "";
        if (obj.containsKey("prikey")) {
            prikey = (String) obj.get("prikey");
        }
        String code_addr = "";
        if (obj.containsKey("code_addr")) {
            code_addr = (String) obj.get("code_addr");
        }

        //
        ContractCompany existed = contractService.getCompany(dnaid);
        if (existed == null) {
            rst.setErrorAndDesc(ErrorCode.DNAID_NOT_EXIST);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        if (!StringUtils.isEmpty(prikey))
            existed.setPrikey(prikey);
        if (!StringUtils.isEmpty(code_addr))
            existed.setCodeAddr(code_addr);
        existed.setUpdateTime(new Date());

        //
        contractService.saveCompany(existed);

        //
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/attestation/company/get")
    public ResponseEntity<Result> getCompany(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getCompany");

        //
        Set<String> required = new HashSet<>();
        required.add("dnaid");
        //
        required.add("ont_password");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String dnaid = (String) obj.get("dnaid");

        //
        ContractCompany company = contractService.getCompany(dnaid);

        //
        rst.setResult(company);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

}
