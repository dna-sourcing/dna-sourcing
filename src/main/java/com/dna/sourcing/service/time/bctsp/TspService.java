package com.dna.sourcing.service.time.bctsp;

import com.dna.sourcing.service.util.PropertiesService;
import com.dna.sourcing.util._codec.CodecUtil;
import org.apache.commons.codec.DecoderException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.bc.BcRSASignerInfoVerifierBuilder;
import org.bouncycastle.operator.*;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.tsp.*;
import org.bouncycastle.util.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TspService {

    //
    private Logger logger = LoggerFactory.getLogger(TspService.class);

    //
    private PropertiesService propertiesService;

    @Autowired
    public TspService(PropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    //
    private int index = 0;

    private String getNextUrlByIndex(int index) {
        return propertiesService.TIMESTAMP_URL_LIST[index];
    }

    private String getNextUrl() {
        return propertiesService.TIMESTAMP_URL_LIST[getNextIndex()];
    }

    private int getNextIndex() {
        if (index + 1 == propertiesService.TIMESTAMP_URL_LIST.length) {
            index = -1;
        }
        return ++index;
    }

    public TimeStampResponse getTimeStampResponseRFC3161(String filehash) throws TSPException, IOException, DecoderException {

        String tsaUrl = getNextUrlByIndex(0);  // 优先使用第一个
        int retry = 0;
        TimeStampResponse timeStampResponse = null;
        while (true) {

            //
            if (timeStampResponse != null || retry >= propertiesService.TIMESTAMP_URL_LIST.length) {
                break;
            }

            //
            logger.info("开始使用时间戳地址为：{}", tsaUrl);
            timeStampResponse = TspClient.getTSAResponse(filehash, tsaUrl);

            //
            tsaUrl = getNextUrl();
            retry++;
            logger.info("retry次数为：{}", retry);
        }
        return timeStampResponse;
    }


    public Map<String, Object> getTimeStampMap(String filehash) throws IOException, DecoderException, TSPException {

        // cannot connect public network

//        TimeStampResponse timeStampResponse = getTimeStampResponseRFC3161(filehash);
//        TimeStampToken timeStampToken = timeStampResponse.getTimeStampToken();

        // 验证
//        validate(timeStampToken);

        //
//        long timestamp = parseToken(timeStampToken);
        long timestamp = System.currentTimeMillis();

        //
        Map<String, Object> map = new HashMap<String, Object>();

        //
        String timeStampResponseToHexStr = "44c03042304002010230360c34746865207265717565737465642054534120706f6c696379206973206e6f7420737570706f72011c38627920746865205453410303000001";
        map.put("timestamp", timestamp);
        map.put("timestampSign", timeStampResponseToHexStr);

        //
        return map;
    }

    public long parseToken(TimeStampToken timeStampToken) {

        //
        TimeStampTokenInfo tstInfo = timeStampToken.getTimeStampInfo();

        //
        Date date = tstInfo.getGenTime();
        long timestamp = date.getTime() / 1000L;
        // System.out.println(timestamp);  // 1554890625

        //
        GeneralName tsa = tstInfo.getTsa();
        // System.out.println(tsa);  // null

        //
        BigInteger serial = tstInfo.getSerialNumber();
        // System.out.println(serial);  // 7944521590396644034

        //
        ASN1ObjectIdentifier oid_policy = tstInfo.getPolicy();
        // System.out.println(oid_policy);  // 1.3.6.1.4.1.13762.3


        CMSSignedData signedData = timeStampToken.toCMSSignedData();
        ContentInfo contentInfo = signedData.toASN1Structure();
        ASN1ObjectIdentifier type = contentInfo.getContentType();
        ASN1Encodable content = contentInfo.getContent();

        SignerId signerId = timeStampToken.getSID();
        AttributeTable attributeTable = timeStampToken.getSignedAttributes();
        AttributeTable unsignedAttributeTable = timeStampToken.getUnsignedAttributes();

        //
        return timestamp;
    }

    public void validate(TimeStampToken timeStampToken) {

        // 获取回传的certificate
        Store store = timeStampToken.getCertificates();
        System.out.println(store);  // org.bouncycastle.util.CollectionStore@6b989889
        if (store == null)
            return;

        // security identifier (SID)  // 通过SID去找对应的certificate
        SignerId sid = timeStampToken.getSID();
        System.out.println(sid);  // org.bouncycastle.cms.SignerId@b95d635

        //
        Collection collection = store.getMatches(sid);
        System.out.println(collection);  // [org.bouncycastle.cert.X509CertificateHolder@62d1ee13]

        //
        Iterator it = collection.iterator();
        X509CertificateHolder remote_cert_holder = (X509CertificateHolder) it.next();
        System.out.println(remote_cert_holder);  // org.bouncycastle.cert.X509CertificateHolder@62d1ee13

        //
        DefaultCMSSignatureAlgorithmNameGenerator generator = new DefaultCMSSignatureAlgorithmNameGenerator();
        DefaultSignatureAlgorithmIdentifierFinder dsaif = new DefaultSignatureAlgorithmIdentifierFinder();
        DefaultDigestAlgorithmIdentifierFinder ddaif = new DefaultDigestAlgorithmIdentifierFinder();
        BcDigestCalculatorProvider provider = new BcDigestCalculatorProvider();
        BcRSASignerInfoVerifierBuilder builder = new BcRSASignerInfoVerifierBuilder(generator, dsaif, ddaif, provider);

        //
        SignerInformationVerifier signerInformationVerifier = null;
        try {
            signerInformationVerifier = builder.build(remote_cert_holder);
        } catch (OperatorCreationException e) {
            logger.error(e.getMessage());
        }
        //Signature processing with JCA and other provider
        //X509CertificateHolder holderJca = new JcaX509CertificateHolder(cert);
        //SignerInformationVerifier sivJca = new JcaSimpleSignerInfoVerifierBuilder().setProvider("anotherprovider").build(holderJca);

        //
        try {
            //
            if (signerInformationVerifier != null) {
                timeStampToken.validate(signerInformationVerifier);
            }
            //
            boolean b = timeStampToken.isSignatureValid(signerInformationVerifier);
            System.out.println(b);
            // Note: this is a much weaker proof of correctness than calling validate().
        } catch (TSPValidationException e) {
            // if the certificate or signature fail to be valid.
            logger.error(e.getMessage());
        } catch (TSPException e) {
            // if an exception occurs in processing the token.
            logger.error(e.getMessage());
        }

        // System.out.println("true ....");
    }


    public byte[] timeStampResponseToBytes(TimeStampResponse timeStampResponse) throws IOException {
        // return the ASN.1 encoded representation of this object.
        byte[] bytes = timeStampResponse.getEncoded();
        //
        return bytes;
    }

    public TimeStampResponse bytesToTimeStampResponse(byte[] bytes) throws IOException, TSPException {
        //
        TimeStampResponse timeStampResponse = new TimeStampResponse(bytes);
        //
        return timeStampResponse;
    }

    public String timeStampResponseToHexStr(TimeStampResponse timeStampResponse) throws IOException {
        //
        byte[] bytes = timeStampResponseToBytes(timeStampResponse);
        //
        String timeStampResponseHexStr = CodecUtil.bytesToHexStr(bytes);
        //
        return timeStampResponseHexStr;
    }

    public TimeStampResponse HexStrToTimeStampResponse(String timeStampResponseHexStr) throws DecoderException, IOException, TSPException {
        //
        byte[] bytes = CodecUtil.hexStrToBytes(timeStampResponseHexStr);
        //
        TimeStampResponse timeStampResponse = bytesToTimeStampResponse(bytes);
        //
        return timeStampResponse;
    }

    // 压缩后，写入数据库
    public String timeStampResponseToHexStrCompressed(TimeStampResponse timeStampResponse) throws IOException {
        //
        byte[] b1 = timeStampResponseToBytes(timeStampResponse);
        //
        byte[] b2 = CodecUtil.compress(b1);
        //
        String s = CodecUtil.bytesToHexStr(b2);
        //
        return s;
    }

    // 从数据库读取后，解压，还原
    public TimeStampResponse hexStrCompressedToTimeStampResponse(String timeStampResponseHexStrCompressed) throws DecoderException, IOException, TSPException {
        //
        byte[] b1 = CodecUtil.hexStrToBytes(timeStampResponseHexStrCompressed);
        //
        byte[] b2 = CodecUtil.uncompress(b1);
        //
        TimeStampResponse timeStampResponse = bytesToTimeStampResponse(b2);
        //
        return timeStampResponse;
    }

    /**
     * 根据指定的Unix时间戳、指定的格式、和指定的时区，获取当前时间
     * @param timestamp   时间戳如果直接输入数字，需要在最后加上L，如1506583155623L
     * @param DATE_FORMAT 格式，如yyyyMMddHHmmss
     * @param TIME_ZONE   时区，如GMT+8
     * @return 时间字符串
     */
    public String getTimeByUnixTimestampAndFormatAndTimeZone(long timestamp, String DATE_FORMAT, String TIME_ZONE) {

        Date now = new Date(timestamp);

        //
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

        // 设置时区
        formatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));

        //
        return formatter.format(now);
    }

}
