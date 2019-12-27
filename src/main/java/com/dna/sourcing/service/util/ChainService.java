package com.dna.sourcing.service.util;

import ch.qos.logback.classic.Logger;
import com.github.ontio.OntSdk;
import com.github.ontio.common.Address;
import com.github.ontio.common.Common;
import com.github.ontio.common.ErrorCode;
import com.github.ontio.common.Helper;
import com.github.ontio.crypto.Curve;
import com.github.ontio.crypto.ECC;
import com.github.ontio.sdk.exception.SDKException;
import com.github.ontio.sdk.wallet.Control;
import com.github.ontio.sdk.wallet.Identity;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.UUID;

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

    public Identity createIdentity(String password) throws Exception {
        String label = "";
        byte[] prikey = ECC.generateKey();
        byte[] salt = ECC.generateKey(16);
        com.github.ontio.account.Account account = new com.github.ontio.account.Account(prikey, ontSdk.getWalletMgr().getSignatureScheme());
        com.github.ontio.sdk.wallet.Account acct;
        switch (ontSdk.getWalletMgr().getSignatureScheme()) {
            case SHA256WITHECDSA:
                acct = new com.github.ontio.sdk.wallet.Account("ECDSA", new Object[]{Curve.P256.toString()}, "aes-256-gcm", "SHA256withECDSA", "sha256");
                break;
            case SM3WITHSM2:
                acct = new com.github.ontio.sdk.wallet.Account("SM2", new Object[]{Curve.SM2P256V1.toString()}, "aes-256-gcm", "SM3withSM2", "sha256");
                break;
            default:
                throw new SDKException(ErrorCode.OtherError("scheme type error"));
        }
        if (password != null) {
            acct.key = account.exportGcmEncryptedPrikey(password, salt, ontSdk.getWalletMgr().getWalletFile().getScrypt().getN());
            password = null;
        } else {
            acct.key = Helper.toHexString(account.serializePrivateKey());
        }
        acct.address = Address.addressFromPubKey(account.serializePublicKey()).toBase58();
        if (label == null || label.equals("")) {
            String uuidStr = UUID.randomUUID().toString();
            label = uuidStr.substring(0, 8);
        }

        Identity idt = new Identity();
        idt.ontid = Common.didont + acct.address;
        idt.label = label;

        idt.controls = new ArrayList<Control>();
        Control ctl = new Control(acct.key, "keys-1", Helper.toHexString(account.serializePublicKey()));
        ctl.setSalt(salt);
        ctl.setAddress(acct.address);
        idt.controls.add(ctl);
        return idt;
    }
}
