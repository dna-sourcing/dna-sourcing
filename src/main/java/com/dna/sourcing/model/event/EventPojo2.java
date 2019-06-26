package com.dna.sourcing.model.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;

public class EventPojo2 {

    @SerializedName("GasConsumed") @Expose private BigInteger gasConsumed;
    @SerializedName("event") @Expose private List<Notify> notify = null;
    @SerializedName("TxHash") @Expose private String txHash;
    @SerializedName("State") @Expose private Integer state;

    public BigInteger getGasConsumed() {
        return gasConsumed;
    }

    public void setGasConsumed(BigInteger gasConsumed) {
        this.gasConsumed = gasConsumed;
    }

    public List<Notify> getNotify() {
        return notify;
    }

    public void setNotify(List<Notify> notify) {
        this.notify = notify;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

}