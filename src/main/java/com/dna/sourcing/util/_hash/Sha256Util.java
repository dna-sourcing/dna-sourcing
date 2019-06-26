package com.dna.sourcing.util._hash;

import com.github.ontio.crypto.Digest;

public class Sha256Util {

    public static String sha256(String data) {
        byte[] bytes = Digest.sha256(data.getBytes());
        return com.github.ontio.common.Helper.toHexString(bytes);
    }
}
