package com.tuan1611pupu.appbansach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Publisher implements Serializable {

    @SerializedName("publisherId")
    @Expose
    private Integer publisherId;
    @SerializedName("publisherName")
    @Expose
    private String publisherName;

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

}