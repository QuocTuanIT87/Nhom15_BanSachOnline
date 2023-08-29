package com.tuan1611pupu.appbansach.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("invoices")
    @Expose
    private List<Order> invoices;
    @SerializedName("carts")
    @Expose
    private List<Object> carts;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("normalizedUserName")
    @Expose
    private String normalizedUserName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("normalizedEmail")
    @Expose
    private String normalizedEmail;
    @SerializedName("emailConfirmed")
    @Expose
    private Boolean emailConfirmed;
    @SerializedName("passwordHash")
    @Expose
    private String passwordHash;
    @SerializedName("securityStamp")
    @Expose
    private String securityStamp;
    @SerializedName("concurrencyStamp")
    @Expose
    private String concurrencyStamp;
    @SerializedName("phoneNumber")
    @Expose
    private Object phoneNumber;
    @SerializedName("phoneNumberConfirmed")
    @Expose
    private Boolean phoneNumberConfirmed;
    @SerializedName("twoFactorEnabled")
    @Expose
    private Boolean twoFactorEnabled;
    @SerializedName("lockoutEnd")
    @Expose
    private Object lockoutEnd;
    @SerializedName("lockoutEnabled")
    @Expose
    private Boolean lockoutEnabled;
    @SerializedName("accessFailedCount")
    @Expose
    private Integer accessFailedCount;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public List<Order> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Order> invoices) {
        this.invoices = invoices;
    }

    public List<Object> getCarts() {
        return carts;
    }

    public void setCarts(List<Object> carts) {
        this.carts = carts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNormalizedUserName() {
        return normalizedUserName;
    }

    public void setNormalizedUserName(String normalizedUserName) {
        this.normalizedUserName = normalizedUserName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNormalizedEmail() {
        return normalizedEmail;
    }

    public void setNormalizedEmail(String normalizedEmail) {
        this.normalizedEmail = normalizedEmail;
    }

    public Boolean getEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(Boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSecurityStamp() {
        return securityStamp;
    }

    public void setSecurityStamp(String securityStamp) {
        this.securityStamp = securityStamp;
    }

    public String getConcurrencyStamp() {
        return concurrencyStamp;
    }

    public void setConcurrencyStamp(String concurrencyStamp) {
        this.concurrencyStamp = concurrencyStamp;
    }

    public Object getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Object phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getPhoneNumberConfirmed() {
        return phoneNumberConfirmed;
    }

    public void setPhoneNumberConfirmed(Boolean phoneNumberConfirmed) {
        this.phoneNumberConfirmed = phoneNumberConfirmed;
    }

    public Boolean getTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public Object getLockoutEnd() {
        return lockoutEnd;
    }

    public void setLockoutEnd(Object lockoutEnd) {
        this.lockoutEnd = lockoutEnd;
    }

    public Boolean getLockoutEnabled() {
        return lockoutEnabled;
    }

    public void setLockoutEnabled(Boolean lockoutEnabled) {
        this.lockoutEnabled = lockoutEnabled;
    }

    public Integer getAccessFailedCount() {
        return accessFailedCount;
    }

    public void setAccessFailedCount(Integer accessFailedCount) {
        this.accessFailedCount = accessFailedCount;
    }

}