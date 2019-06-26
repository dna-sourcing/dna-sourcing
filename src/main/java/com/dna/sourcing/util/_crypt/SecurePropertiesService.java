package com.dna.sourcing.util._crypt;

import com.dna.sourcing.util._codec.Base64ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * 通信加密使用的参数
 */
@Service
public class SecurePropertiesService {

    private static final Logger logger = LoggerFactory.getLogger(SecurePropertiesService.class);

    @Autowired
    public SecurePropertiesService(@Value("${com.dna.sourcing.dnaid.rsa.publicKey}") String rsaPublicKey,
                                   @Value("${com.dna.sourcing.dnaid.rsa.privateKey}") String rsaPrivateKey,
                                   @Value("${com.dna.sourcing.dnaid.aes.iv}") String aesIV) {
        //
        SecurePropertiesService.RSA_PUBLIC_KEY = rsaPublicKey;
        SecurePropertiesService.RSA_PRIVATE_KEY = rsaPrivateKey;
        //
        SecurePropertiesService.AES_IV = aesIV;
    }

    // @Autowired
    // private Environment environment;
    //
    // public boolean isProEnv() {
    //     String[] activeProfiles = environment.getActiveProfiles();
    //     for (String activeProfile : activeProfiles) {
    //         if (activeProfile.equals("pro")) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    private static String RSA_PUBLIC_KEY;

    public String getRsaPublicKey() {
        try {
            return Base64ConvertUtil.decode(SecurePropertiesService.RSA_PUBLIC_KEY);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            logger.error(e.getMessage());
            return null;
        }
    }


    private static String RSA_PRIVATE_KEY;

    public String getRsaPrivateKey() {
        try {
            return Base64ConvertUtil.decode(SecurePropertiesService.RSA_PRIVATE_KEY);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            logger.error(e.getMessage());
            return null;
        }
    }

    // private static String WALLET_GO_PUBLIC_KEY;
    //
    // @Value("${wallet.goPublicKey}")
    // public void setWalletGoPublicKey(String publicKey) {
    //     SecureConfig.WALLET_GO_PUBLIC_KEY = publicKey;
    // }
    //
    // public String getWalletGoPublicKey() {
    //     try {
    //         return Base64ConvertUtil.decode(SecureConfig.WALLET_GO_PUBLIC_KEY);
    //     } catch (UnsupportedEncodingException e) {
    //         logger.error(e.getMessage());
    //         logger.error(e.getMessage());
    //         return null;
    //     }
    // }

    // private static String WALLET_JAVA_PRIVATE_KEY;
    //
    // @Value("${wallet.javaPrivateKey}")
    // public void setWalletJavaPrivateKey(String privateKey) {
    //     SecureConfig.WALLET_JAVA_PRIVATE_KEY = privateKey;
    // }
    //
    // public String getWalletJavaPrivateKey() {
    //     try {
    //         return Base64ConvertUtil.decode(SecureConfig.WALLET_JAVA_PRIVATE_KEY);
    //     } catch (UnsupportedEncodingException e) {
    //         logger.error(e.getMessage());
    //         logger.error(e.getMessage());
    //         return null;
    //     }
    // }

    private static String AES_IV;

    public String getAesIv() {
        try {
            return Base64ConvertUtil.decode(SecurePropertiesService.AES_IV);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            logger.error(e.getMessage());
            return null;
        }
    }

    // private static String AES_KEY;
    //
    // @Value("${aes.key}")
    // public void setAesKey(String key) {
    //     SecureConfig.AES_KEY = key;
    // }
    //
    // public String getAesKey() {
    //     try {
    //         return Base64ConvertUtil.decode(SecureConfig.AES_KEY);
    //     } catch (UnsupportedEncodingException e) {
    //         logger.error(e.getMessage());
    //         logger.error(e.getMessage());
    //         return null;
    //     }
    // }


    // private static String WALLET_ONTID;
    //
    // @Value("${wallet.dnaid}")
    // public void setWalletDnaid(String walletDnaid) {
    //     SecureConfig.WALLET_ONTID = walletDnaid;
    // }
    //
    // public String getWalletDnaid() {
    //     try {
    //         return Base64ConvertUtil.decode(SecureConfig.WALLET_ONTID);
    //     } catch (UnsupportedEncodingException e) {
    //         logger.error(e.getMessage());
    //         logger.error(e.getMessage());
    //         return null;
    //     }
    // }


}
