package com.dna.sourcing.service.util;

import ch.qos.logback.classic.Logger;
import com.github.ontio.OntSdk;
import com.github.ontio.common.Common;
import com.github.ontio.sdk.exception.SDKException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ChainService {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(ChainService.class);

    //
    PropertiesService propertiesService;

    @Autowired
    public ChainService(PropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    @PostConstruct
    void whatever() {
        initOntSdk(propertiesService.ontologyUrlList[0], propertiesService.walletPath);
    }

    //
    private volatile int index = 0;

    private String getNextUrlByIndex(int index) {
        return propertiesService.ontologyUrlList[index];
    }

    private String getNextUrl() {
        return propertiesService.ontologyUrlList[getNextIndex()];
    }

    private synchronized int getNextIndex() {
        if (index + 1 == propertiesService.ontologyUrlList.length) {
            index = -1;
        }
        return ++index;
    }

    //
    public volatile OntSdk ontSdk;


    //
    private void initOntSdk(String ontologyUrl, String walletPath) {

        //
        ontSdk = getOntSdk(ontologyUrl, walletPath);
    }

    //
    public void switchOntSdk() {

        //
        logger.error("switchOntSdk start ... , old ontSdk is {}", ontSdk);

        //
        String ontologyUrl = getNextUrl();
        String walletPath = propertiesService.walletPath;

        //
        ontSdk = getOntSdk(ontologyUrl, walletPath);

        //
        logger.error("switchOntSdk start ... , new ontSdk is {}", ontSdk);
        // TODO ontSDK 变量标识没有变，但里面的url确实已被替换，可能是由于下面的getInstance单例
    }

    //
    private OntSdk getOntSdk(String ontologyUrl, String walletPath) {

        //
        String restUrl = ontologyUrl + ":" + "20334";
        String rpcUrl = ontologyUrl + ":" + "20336";
        String wsUrl = ontologyUrl + ":" + "20335";

        OntSdk wm = OntSdk.getInstance();
        wm.setRpc(rpcUrl);
        wm.setRestful(restUrl);
        try {
            wm.setDefaultConnect(wm.getRestful());
        } catch (SDKException e) {
            logger.error(e.getMessage());
        }

        wm.openWalletFile(walletPath);

        //
        Common.didont = "did:dna:";

        return wm;
    }

}
