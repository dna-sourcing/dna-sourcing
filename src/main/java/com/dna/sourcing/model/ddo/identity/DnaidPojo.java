package com.dna.sourcing.model.ddo.identity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DnaidPojo {

    @SerializedName("controls")
    @Expose
    private List<Control> controls = null;
    @SerializedName("isDefault")
    @Expose
    private Boolean isDefault;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("lock")
    @Expose
    private Boolean lock;
    @SerializedName("dnaid")
    @Expose
    private String dnaid;

    public List<Control> getControls() {
        return controls;
    }

    public void setControls(List<Control> controls) {
        this.controls = controls;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public String getDnaid() {
        return dnaid;
    }

    public void setDnaid(String dnaid) {
        this.dnaid = dnaid;
    }

}