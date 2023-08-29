package com.tuan1611pupu.appbansach.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignInRequest implements Serializable {
    @SerializedName("UserName")
    private String userName;

    @SerializedName("Password")
    private String password;

    @SerializedName("Email")
    private String email;

    @SerializedName("FullName")
    private String fullName;


    public SignInRequest() {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
    }

    // Getters và setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
