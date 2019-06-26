package com.dna.sourcing.model.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExceptionMsg {

    @SerializedName("Desc") @Expose private String desc;
    @SerializedName("Error") @Expose private Integer error;

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

}
