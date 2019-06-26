package com.dna.sourcing.util._codec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.xerial.snappy.Snappy;

import java.io.IOException;

public class CodecUtil {


    public static String bytesToHexStr(byte[] bytes) {
        //
        String hexString = Hex.encodeHexString(bytes);
        //
        return hexString;
    }

    public static byte[] hexStrToBytes(String hexStr) throws DecoderException {
        //
        byte[] bytes = Hex.decodeHex(hexStr);
        //
        return bytes;
    }

    // 压缩
    public static byte[] compress(byte[] in) throws IOException {

        byte[] compressed = Snappy.compress(in);
        return compressed;
    }

    // 解压缩
    public static byte[] uncompress(byte[] in) throws IOException {

        byte[] out = Snappy.uncompress(in);
        return out;
    }

}
