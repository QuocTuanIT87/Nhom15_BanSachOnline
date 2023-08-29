package com.tuan1611pupu.appbansach.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Invoice implements Serializable {
        @SerializedName("memberId")
        private String memberId;
        @SerializedName("email")
        private String email;
        @SerializedName("tel")
        private String tel;

        @SerializedName("address")
        private String address;


        public Invoice() {
            this.memberId = memberId;
            this.tel = tel;
            this.email = email;
            this.address = address;
        }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
