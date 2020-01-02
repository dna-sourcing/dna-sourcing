package com.dna.sourcing.service;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.dna.sourcing.dao.Event;
import com.dna.sourcing.dao.contract.Contract;
import com.dna.sourcing.dao.contract.ContractCompany;
import com.dna.sourcing.dao.ddo.ActionDnaid;
import com.dna.sourcing.mapper.EventMapper;
import com.dna.sourcing.mapper.contract.ContractCompanyMapper;
import com.dna.sourcing.mapper.contract.ContractMapper;
import com.dna.sourcing.mapper.ddo.ActionDnaidMapper;
import com.dna.sourcing.service.oauth.JWTService;
import com.dna.sourcing.service.util.ChainService;
import com.dna.sourcing.service.util.PropertiesService;
import com.dna.sourcing.util.GlobalVariable;
import com.dna.sourcing.util._hash.Sha256Util;
import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.sdk.exception.SDKException;
import com.github.ontio.sdk.wallet.Identity;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import com.github.pagehelper.PageHelper;
import com.lambdaworks.crypto.SCryptUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.*;

@Service
public class ContractService {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(ContractService.class);

    //
    private ChainService chainService;
    private JWTService jwtService;

    //
    private ContractMapper contractMapper;
    private EventMapper eventMapper;
    private ContractCompanyMapper contractCompanyMapper;
    private ActionDnaidMapper actionDnaidMapper;

    // 公共payer和公共合约
    private String codeAddr;
    private Account payer;

    @Autowired
    public ContractService(PropertiesService propertiesService,
                           ChainService chainService,
                           JWTService jwtService,
                           ContractMapper contractMapper,
                           EventMapper eventMapper,
                           ContractCompanyMapper contractCompanyMapper,
                           ActionDnaidMapper actionDnaidMapper) {
        //
        this.chainService = chainService;
        this.jwtService = jwtService;

        //
        this.contractMapper = contractMapper;
        this.eventMapper = eventMapper;
        this.contractCompanyMapper = contractCompanyMapper;
        this.actionDnaidMapper = actionDnaidMapper;

        // 合约哈希/合约地址/contract codeAddr
        codeAddr = propertiesService.codeAddr;
        payer = GlobalVariable.getInstanceOfAccount(propertiesService.payerPrivateKey);
    }

    //
    public Map<String, String> createDnaid(String username,
                                           String password) throws Exception {

        // 检查用户名是否已存在
        Example example = new Example(ActionDnaid.class);
        example.createCriteria().andCondition("username=", username);
        int total = actionDnaidMapper.selectCountByExample(example);
        if (total > 0) {
            throw new Exception("username already existed.");
        }

        //
        Map<String, String> map = new HashMap<String, String>();

        // 创建数字身份 identity
        Identity identity = chainService.createIdentity(password);

        //
        map.put("txhash", "");

        //
        String keystore = JSON.toJSONString(identity);
        map.put("keystore", JSON.toJSONString(identity));
        String dnaid = identity.ontid;
        map.put("dnaid", dnaid);

        // 写入本地表
        ActionDnaid record = new ActionDnaid();
        record.setUsername(username);
        record.setPassword(SCryptUtil.scrypt(password, GlobalVariable.scrypt_N, GlobalVariable.scrypt_r, GlobalVariable.scrypt_p));
        record.setDnaid(dnaid);
        record.setKeystore(keystore);
        record.setTxhash("");
        record.setActionIndex(1);
        record.setCreateTime(new Date());
        actionDnaidMapper.insertSelective(record);

        //
        return map;
    }

    //
    public void loginByDnaid(String user_dnaid,
                             String password) throws Exception {

        // 先检查user_dnaid是否已注册
        Example example = new Example(ActionDnaid.class);
        example.createCriteria().andCondition("dnaid=", user_dnaid);
        ActionDnaid existed = actionDnaidMapper.selectOneByExample(example);
        if (existed == null) {
            throw new Exception("user_dnaid not exist.");
        }

        // String tmp = SCryptUtil.scrypt(password, GlobalVariable.scrypt_N, GlobalVariable.scrypt_r, GlobalVariable.scrypt_p);
        if (!SCryptUtil.check(password, existed.getPassword())) {
            throw new Exception("password error.");
        }
    }

    //
    public Map<String, String> login(String username,
                                     String password) throws Exception {

        // 先检查username是否已注册
        Example example = new Example(ActionDnaid.class);
        example.createCriteria().andCondition("username=", username);
        ActionDnaid existed = actionDnaidMapper.selectOneByExample(example);
        if (existed == null) {
            throw new Exception("username not exist.");
        }

        // String tmp = SCryptUtil.scrypt(password, GlobalVariable.scrypt_N, GlobalVariable.scrypt_r, GlobalVariable.scrypt_p);
        if (!SCryptUtil.check(password, existed.getPassword())) {
            throw new Exception("password error.");
        }

        //
        String user_dnaid = existed.getDnaid();

        Map<String, String> map = jwtService.getAccessToken(user_dnaid);

        return map;
    }

    //
    public Map<String, String> putContract(Contract contract) throws Exception {

        // 上链时，只把指定field的组合后的hash作为key
        String c_key = contractToDigestForKey(contract);
        // 上链时，只把指定field的组合后的hash作为value
        String c_value = contractToDigestForValue(contract);

        //
        return putContract(contract, c_key, c_value);
    }

    public Map<String, String> putContract(Contract contract,
                                           String c_key,
                                           String c_value) throws Exception {

        // 先查询是不是项目方，有没有设置指定的payer地址和合约地址
        // String c_dnaid = contract.getCompanyDnaid();
        // //
        // Example example = new Example(ContractCompany.class);
        // Example.Criteria criteria = example.createCriteria();
        // criteria.andCondition("dnaid=", c_dnaid);
        // ContractCompany contractCompany = contractCompanyMapper.selectOneByExample(example);
        // //
        // if (contractCompany != null) {
        //     payer = GlobalVariable.getInstanceOfAccount(contractCompany.getPrikey());
        //     codeAddr = contractCompany.getCodeAddr();
        //     // String s1 = payer.getAddressU160().toBase58();
        // } else {
        //     // TODO
        //     throw new Exception("项目方地址列表中找不到该dnaid..." + c_dnaid);
        // }
        // String s2 = payer.getAddressU160().toBase58();

        //
        logger.debug("c_key is {}", c_key);
        logger.debug("c_value is {}", c_value);

        //
        List paramList = new ArrayList<>();
        paramList.add("putRecord".getBytes());

        List args = new ArrayList();
        // key
        args.add(c_key);
        // value
        args.add(c_value);

        //
        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        //
        Map<String, String> map = invokeContract(Helper.reverse(codeAddr), null, params, payer, 76220L, GlobalVariable.DEFAULT_GAS_PRICE, false);

        //
        String txhash = map.get("txhash");
        String result = map.get("result");
        // System.out.println(result);  // true

        //
        return map;
    }

    public String getContract(Contract contract,
                              Account payer) throws Exception {

        List paramList = new ArrayList<>();
        paramList.add("getRecord".getBytes());

        List args = new ArrayList();
        args.add(contractToDigestForKey(contract));

        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        // 先查询是不是项目方，有没有设置指定的payer地址和合约地址
        // Example example = new Example(ContractCompany.class);
        // Example.Criteria criteria = example.createCriteria();
        // criteria.andCondition("dnaid=", contract.getCompanyDnaid());
        // ContractCompany contractCompany = contractCompanyMapper.selectOneByExample(example);
        // //
        // if (contractCompany != null) {
        //     payer = GlobalVariable.getInstanceOfAccount(contractCompany.getPrikey());
        //     codeAddr = Address.AddressFromVmCode(contractCompany.getCodeAddr()).toHexString();
        // }

        //
        Map<String, String> map = invokeContract(Helper.reverse(codeAddr), null, params, payer, chainService.ontSdk.DEFAULT_GAS_LIMIT, GlobalVariable.DEFAULT_GAS_PRICE, true);

        //
        String txhash = map.get("txhash");
        String result = map.get("result");

        //
        String s1 = JSON.parseObject(result).getString("Result");
        byte[] s2 = Helper.hexToBytes(s1);
        String value = new String(s2);

        //
        return value;
    }

    //
    public Map<String, String> invokeContract(String codeAddr,
                                              String method,
                                              byte[] params,
                                              Account payerAcct,
                                              long gaslimit,
                                              long gasprice,
                                              boolean preExec) throws Exception {

        //
        if (payerAcct == null) {
            throw new SDKException("params should not be null");
        }
        if (gaslimit < 0 || gasprice < 0) {
            throw new SDKException("gaslimit or gasprice should not be less than 0");
        }

        //
        Map<String, String> map = new HashMap<String, String>();

        //
        Transaction tx = chainService.ontSdk.vm().makeInvokeCodeTransaction(codeAddr, method, params, payerAcct.getAddressU160().toBase58(), gaslimit, gasprice);
        // System.out.println(tx);
        // com.github.ontio.core.payload.InvokeCode@fecc2faa

        //
        chainService.ontSdk.addSign(tx, payerAcct);

        // String s = payerAcct.getAddressU160().toBase58();

        //
        String rawdata = tx.toHexString();
        String txhash = tx.hash().toString();
        //
        map.put("txhash", txhash);
        map.put("rawdata", rawdata);  // TODO

        //
        if (preExec) {

            for (int retry = 0; retry < 5; retry++) {
                //
                Object result = null;
                try {
                    result = chainService.ontSdk.getConnect().sendRawTransactionPreExec(rawdata);
                } catch (ConnectorException | IOException e) {
                    logger.error(e.getMessage());
                    //
                    chainService.switchOntSdk();
                    //
                    continue;
                }
                //
                map.put("result", result.toString());
                //
                break;
            }

        } else {

            for (int retry = 0; retry < 5; retry++) {
                //
                boolean result = false;
                try {
                    result = chainService.ontSdk.getConnect().sendRawTransaction(rawdata);
                } catch (ConnectorException | IOException e) {
                    logger.error(e.getMessage());
                    //
                    chainService.switchOntSdk();
                    //
                    continue;
                }
                //
                map.put("result", Boolean.toString(result));
                //
                break;
            }
        }

        //
        return map;
    }

    // 发到链上的key
    public String contractToDigestForKey(Contract contract) {
        String k = contract.getDnaid() + contract.getCompanyDnaid() + contract.getFilehash() + contract.getTimestamp();
        return Sha256Util.sha256(k);
    }

    // 发到链上的value
    public String contractToDigestForValue(Contract contract) {
        String v = contract.getDnaid() + contract.getCompanyDnaid() + contract.getDetail() + contract.getTimestamp() + contract.getTimestampSign();
        return Sha256Util.sha256(v);
    }

    // 后期如果需要验证
    public boolean verifyContractOnBlockchain(Contract contract,
                                              Account payer) throws Exception {
        String valueLocal = contractToDigestForValue(contract);
        String valueOnBlockchain = getContract(contract, payer);
        return valueOnBlockchain.equals(valueLocal);
    }

    //
    public List<Contract> getHistoryByDnaid(String dnaid,
                                            int pageNum,
                                            int pageSize,
                                            String type) throws Exception {
        //
        Example example = new Example(Contract.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("dnaid=", dnaid);
        c1.andCondition("status=", 0);
        if (!StringUtils.isEmpty(type)) {
            c1.andCondition("type=", type);
        }
        //
        example.setOrderByClause("id desc");
        example.and(c1);
        //
        PageHelper.startPage(pageNum, pageSize, false);
        List<Contract> list = contractMapper.selectByExample(example);

        //
        return addHeight(list);
    }

    //
    public List<Contract> getExplorerHistory(int pageNum,
                                             int pageSize) {
        //
        //
        Example example = new Example(Contract.class);
        example.createCriteria().andCondition("status=", 0);
        example.setOrderByClause("id desc");
        PageHelper.startPage(pageNum, pageSize,false);
        List<Contract> list = contractMapper.selectByExample(example);
        //
        return addHeight(list);
    }

    //
    public List<Contract> selectByDnaidAndTxHash(String dnaid,
                                                 String txhash) {
        //
        Example example = new Example(Contract.class);
        //
        Example.Criteria criteria = example.createCriteria();
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("dnaid=", dnaid);
        c1.andCondition("status=", 0);

        Example.Criteria c2 = example.createCriteria();
        c2.orEqualTo("txhash", txhash).orEqualTo("filehash", txhash);
        //
        example.and(c1);
        example.and(c2);
        //
        List<Contract> list = contractMapper.selectByExample(example);
        //
        return addHeight(list);
    }

    //
    public List<Contract> selectByDnaidAndHash(String dnaid,
                                               String hash) throws Exception {
        //
        Example example = new Example(Contract.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("dnaid=", dnaid);
        c1.andCondition("status=", 0);
        //
        Example.Criteria c2 = example.createCriteria();
        c2.orEqualTo("txhash", hash).orEqualTo("filehash", hash);
        //
        example.and(c1);
        example.and(c2);
        //
        List<Contract> list = contractMapper.selectByExample(example);
        //
        return addHeight(list);
    }

    //
    public int updateStatusByDnaidAndHash(String dnaid,
                                          String hash) {
        //
        Example example = new Example(Contract.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("dnaid=", dnaid);
        // todo 没做state判断
        //
        Example.Criteria c2 = example.createCriteria();
        c2.orEqualTo("txhash", hash).orEqualTo("filehash", hash);
        //
        example.and(c1);
        example.and(c2);
        //
        Contract c = new Contract();
        c.setStatus(1);
        return contractMapper.updateByExampleSelective(c, example);
    }

    //
    public List<Contract> selectByHash(String hash) {

        //
        Example example = new Example(Contract.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andEqualTo("status", 0);
        //
        Example.Criteria c2 = example.createCriteria();
        c2.orEqualTo("txhash", hash).orEqualTo("filehash", hash);
        //
        example.and(c1);
        example.and(c2);
        //
        List<Contract> list = contractMapper.selectByExample(example);
        //
        return addHeight(list);
    }

    //
    public void updateRevokeTx(String txhash,
                               String revokeTx) {
        //
        Example example = new Example(Contract.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("txhash=", txhash);
        //
        example.and(c1);
        //
        Contract c = new Contract();
        c.setRevokeTx(revokeTx);
        contractMapper.updateByExampleSelective(c, example);
    }

    //
    public Integer count(String dnaid) throws Exception {
        //
        Example example = new Example(Contract.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("dnaid=", dnaid);
        c1.andCondition("status=", 0);
        //
        example.and(c1);
        //
        return contractMapper.selectCountByExample(example);
    }

    // 跨表添加height信息
    private List<Contract> addHeight(List<Contract> list) {
        //
        if (list == null || list.size() == 0)
            return list;
        //
        List<Contract> newlist = new ArrayList<>();
        for (Contract c : list) {
            //
            Example example = new Example(Event.class);
            //
            Example.Criteria c1 = example.createCriteria();
            c1.andCondition("txhash=", c.getTxhash());
            //
            example.and(c1);
            //
            Event e = eventMapper.selectOneByExample(example);
            //
            if (e != null) {
                Integer height = e.getHeight();
                c.setHeight(height);
            } else {
                c.setHeight(0);  // TODO
            }
            newlist.add(c);
        }
        return newlist;
    }

    // 写入数据库
    public void saveToLocal(Contract contract) {
        contractMapper.insertSelective(contract);
    }

    // 写入数据库，batch insert
    public void saveToLocalBatch(String dnaid,
                                 List<Contract> contractList) {
        contractMapper.insertBatch(contractList);
    }

    //
    public void saveCompany(ContractCompany company) {
        contractCompanyMapper.insertSelective(company);
    }

    //
    public ContractCompany getCompany(String dnaid) {
        //
        Example example = new Example(ContractCompany.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("dnaid=", dnaid);
        //
        example.and(c1);
        //
        ContractCompany company = contractCompanyMapper.selectOneByExample(example);
        //
        return company;
    }

    // 先查询是不是项目方，有没有设置指定的payer地址和合约地址
    public void checkCompany(String company_dnaid) throws Exception {
        ContractCompany contractCompany = getCompany(company_dnaid);
        if (contractCompany == null) {
            // TODO
            throw new Exception("项目方地址列表中找不到该dnaid..." + company_dnaid);
        }
    }
}
