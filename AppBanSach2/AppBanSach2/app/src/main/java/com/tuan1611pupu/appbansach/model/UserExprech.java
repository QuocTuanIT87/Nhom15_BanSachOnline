package com.tuan1611pupu.appbansach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserExprech implements Serializable {


    @SerializedName("memberId")
    @Expose
    private String memberId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("expiration")
    @Expose
    private String expiration;

    public UserExprech(String memberId, String token, String expiration) {
        this.memberId = memberId;
        this.token = token;
        this.expiration = expiration;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

}

