package com.example.retofit2.dto.requestDTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductRequestDTO {
    @SerializedName("phoneID")
    private int productId;
    @SerializedName("productName")
    private String productName;
    @SerializedName("price")
    private double price;
    @SerializedName("color")
    private String color;
    @SerializedName("description")
    private String description;
    @SerializedName("soldQuantity")
    private int soldQuantity;
    @SerializedName("remainingQuantity")
    private int remainingQuantity;
    @SerializedName("imageProducts")
    private List<String> imageProducts;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public List<String> getImageProducts() {
        return imageProducts;
    }

    public void setImageProducts(List<String> imageProducts) {
        this.imageProducts = imageProducts;
    }
}
