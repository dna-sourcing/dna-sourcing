/**
 * leewi9 2019-03-20
 */

package com.dna.sourcing.model.ddo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("Type") @Expose private String type;
    @SerializedName("Curve") @Expose private String curve;
    @SerializedName("Value") @Expose private String value;
    @SerializedName("PubKeyId") @Expose private String pubKeyId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurve() {
        return curve;
    }

    public void setCurve(String curve) {
        this.curve = curve;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPubKeyId() {
        return pubKeyId;
    }

    public void setPubKeyId(String pubKeyId) {
        this.pubKeyId = pubKeyId;
    }

}