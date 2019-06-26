package com.dna.sourcing.service.time.bctsp;

import com.dna.sourcing.service.time.bctsp.exceptions.ClientException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.asn1.tsp.TimeStampResp;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.tsp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TspClient {

    private static Logger logger = LoggerFactory.getLogger(TspClient.class);

    public static TimeStampResponse getTSAResponse(String filehash, String tsaUrl) throws IOException, TSPException, DecoderException {

        // TODO
        // Thread.currentThread().setUncaughtExceptionHandler(new ExceptionHandler());

        //
        TimeStampRequestGenerator timeStampRequestGenerator = new TimeStampRequestGenerator();
        timeStampRequestGenerator.setCertReq(true);
        timeStampRequestGenerator.setReqPolicy(new ASN1ObjectIdentifier("1.3.6.1.4.1.13762.3"));  // 设置 OID
        BigInteger nonce = generateNonce();
        logger.info("nonce:\n{}", nonce);
        final TimeStampRequest timeStampRequest = timeStampRequestGenerator.generate(TSPAlgorithms.SHA256, Hex.decodeHex(filehash), nonce);

        //
        // logger.info("RFC3161 request ASN1 representation:\n");
        String reqStr = ASN1Dump.dumpAsString(new ASN1InputStream(timeStampRequest.getEncoded()).readObject(), true);
        // logger.info(reqStr);

        // 发送请求
        OutputStream out = null;
        HttpURLConnection con = null;
        URL url = null;
        byte[] request = timeStampRequest.getEncoded();

        url = new URL(tsaUrl);
        //
        con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type", "application/timestamp-query");
        con.setRequestProperty("Content-length", String.valueOf(request.length));
        out = con.getOutputStream();
        out.write(request);
        out.flush();
        //
        if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Received HTTP error: " + con.getResponseCode() + " - " + con.getResponseMessage());
        } else {
            logger.info("Response Code: ".concat(Integer.toString(con.getResponseCode())));
        }
        InputStream in = con.getInputStream();

        //
        TimeStampResp resp = TimeStampResp.getInstance(new ASN1InputStream(in).readObject());
        System.out.println(resp);

        //
        TimeStampResponse timeStampResponse = new TimeStampResponse(resp);
        System.out.println(timeStampResponse);

        //
        PKIFailureInfo failureInfo = timeStampResponse.getFailInfo();
        // System.out.println(failureInfo);
        // TODO 正常会返回null
        // System.out.println(failureInfo.intValue());

        // 这里并不是真正的验签环节
        timeStampResponse.validate(timeStampRequest);
        /*
        Check this response against to see if it a well formed response for the passed in request. Validation will include checking the time stamp token if the response status is GRANTED or GRANTED_WITH_MODS.
         */

        return timeStampResponse;
    }

    private static BigInteger generateNonce() {
        try {
            final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            return new BigInteger(64, random);
        } catch (final NoSuchAlgorithmException exception) {
            throw new ClientException("Failed to initialize SecureRandom instance", exception);
        }
    }
}
