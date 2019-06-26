package com.dna.sourcing.model.ddo.identity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Base64;

public class Control {

    @SerializedName("codeAddr") @Expose private String address;
    @SerializedName("algorithm") @Expose private String algorithm;
    @SerializedName("enc-alg") @Expose private String encAlg;
    @SerializedName("hash") @Expose private String hash;
    @SerializedName("id") @Expose private String id;
    @SerializedName("key") @Expose private String key;
    @SerializedName("parameters") @Expose private Parameters parameters;
    @SerializedName("publicKey") @Expose private String publicKey;
    @SerializedName("salt") @Expose private String salt;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getEncAlg() {
        return encAlg;
    }

    public void setEncAlg(String encAlg) {
        this.encAlg = encAlg;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

//    public String getSalt() {
//        return salt;
//    }
//
//    public void setSalt(String salt) {
//        this.salt = salt;
//    }

    public byte[] getSalt(){
        return Base64.getDecoder().decode(salt);
    }
    public void setSalt(byte[] salt){
        this.salt = new String(Base64.getEncoder().encode(salt));
    }

}