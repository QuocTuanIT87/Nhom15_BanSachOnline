package com.tuan1611pupu.appbansach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookInfo {

    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("publisherName")
    @Expose
    private String publisherName;
    @SerializedName("authorName")
    @Expose
    private String authorName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("descreption")
    @Expose
    private String descreption;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("price")
    @Expose
    private Integer price;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}
