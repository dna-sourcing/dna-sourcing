package com.dna.sourcing.model.contract.input;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InputWrapper {

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @SerializedName("user_dnaid")
    @Expose
    private String userDnaid;

    @SerializedName("filehash")
    @Expose
    private String filehash;

    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    @SerializedName("context")
    @Expose
    private Context context;

    @SerializedName("signature")
    @Expose
    private String signature;

    @SerializedName("type")
    @Expose
    private String type;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserDnaid() {
        return userDnaid;
    }

    public void setUserDnaid(String userDnaid) {
        this.userDnaid = userDnaid;
    }

    public String getFilehash() {
        return filehash;
    }

    public void setFilehash(String filehash) {
        this.filehash = filehash;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}