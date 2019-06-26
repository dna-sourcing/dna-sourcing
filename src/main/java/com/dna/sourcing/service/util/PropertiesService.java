package com.dna.sourcing.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesService {

    // 时间戳
    public final String[] TIMESTAMP_URL_LIST;

    @Autowired
    public PropertiesService(@Value("${com.dna.sourcing.TIMESTAMP_URL_LIST}") String[] timestampUrlList,
                             @Value("${com.dna.sourcing.PAYER_PRIVATE_KEY}") String payerPrivateKey,
                             @Value("${com.dna.sourcing.WALLET_PATH}") String walletPath,
                             @Value("${com.dna.sourcing.ONTOLOGY_URL_LIST}") String[] ontologyUrlList,
                             @Value("${com.dna.sourcing.ONTID_PUBLIC_KEY}") String dnaidPublicKey,
                             @Value("${com.dna.sourcing.CONTRACT_CODE_ADDRESS}") String codeAddr,
                             @Value("${com.dna.sourcing.ONT_PASSWORD}") String ontPassword) {
        //
        this.TIMESTAMP_URL_LIST = timestampUrlList;
        //
        this.payerPrivateKey = payerPrivateKey;
        this.walletPath = walletPath;
        this.ontologyUrlList = ontologyUrlList;
        this.dnaidPublicKey = dnaidPublicKey;
        //
        this.codeAddr = codeAddr;
        //
        this.ontPassword = ontPassword;
    }

    // 付款的数字钱包
    public String payerPrivateKey;

    //
    public String walletPath;


    //
    public final String[] ontologyUrlList;


    /**
     * dnaid后台的公钥,测试网的
     * ONT ID backend public key
     */
    public String dnaidPublicKey;

    // 存证智能合约地址
    public String codeAddr;

    // 项目方添加定制的合约和付款秘钥，需要这个密码进行验证
    public String ontPassword;
}
