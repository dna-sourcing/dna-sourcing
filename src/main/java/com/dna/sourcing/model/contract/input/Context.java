package com.dna.sourcing.model.contract.input;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Context {

    @SerializedName("image")
    @Expose
    private List<String> image = null;
    @SerializedName("text")
    @Expose
    private List<String> text = null;

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

}