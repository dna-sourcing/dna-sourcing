package com.dna.sourcing.model.ddo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DDOPojo {

    @SerializedName("Attributes") @Expose private List<Object> attributes = null;
    @SerializedName("OntId") @Expose private String ontId;
    @SerializedName("Owners") @Expose private List<Owner> owners = null;

    public List<Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Object> attributes) {
        this.attributes = attributes;
    }

    public String getOntId() {
        return ontId;
    }

    public void setOntId(String ontId) {
        this.ontId = ontId;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public void setOwners(List<Owner> owners) {
        this.owners = owners;
    }

}
