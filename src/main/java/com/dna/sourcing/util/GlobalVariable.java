package com.dna.sourcing.util;

import ch.qos.logback.classic.Logger;
import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import com.github.ontio.crypto.SignatureScheme;
import org.slf4j.LoggerFactory;

public class GlobalVariable {

    //
    private static Logger logger = (Logger) LoggerFactory.getLogger(GlobalVariable.class);

    //
    public static String API_VERSION = "1.0.0";

    //
    // public static int DEFAULT_GAS_PRICE = 500;
    public static int DEFAULT_GAS_PRICE = 0;

    //
    private static Account instanceOfAccount = null;

    public static synchronized Account getInstanceOfAccount(String payerPrivateKey) {
        // if (instanceOfAccount == null) {
        //     try {
        //         // 方法一，当时设置的passphrase若已被记录到钱包文件中，所以必须对应
        //         // instanceOfAccount = getOntSdk().getWalletMgr().getAccount(payerAddress, payerPassphrase);
        //         // 方法二，直接从私钥
        //         instanceOfAccount = new Account(Helper.hexToBytes(payerPrivateKey), SignatureScheme.SHA256WITHECDSA);
        //     } catch (Exception e) {
        //         logger.error(e.getMessage());
        //     }
        // }
        // return instanceOfAccount;

        try {
            return new Account(Helper.hexToBytes(payerPrivateKey), SignatureScheme.SHA256WITHECDSA);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    //
    public static Integer scrypt_N = 16384;
    public static Integer scrypt_r = 8;
    public static Integer scrypt_p = 1;

}
