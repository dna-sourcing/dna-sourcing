package com.dna.sourcing.model.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestfulExceptionMsg {

    @SerializedName("Action")
    @Expose
    private String action;
    @SerializedName("Desc")
    @Expose
    private String desc;
    @SerializedName("Error")
    @Expose
    private Integer error;
    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("Version")
    @Expose
    private String version;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
