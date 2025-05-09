package com.example.retofit2.dto.requestDTO;

import com.google.gson.annotations.SerializedName;

public class CartItemUpdateRequestDTO {
    @SerializedName("product_name")
    private String productName;

    @SerializedName("color")
    private String color;

    @SerializedName("new_quantity")
    private int newQuantity;

    // Constructor, getters và setters
    public CartItemUpdateRequestDTO(String productName, String color, int newQuantity) {
        this.productName = productName;
        this.color = color;
        this.newQuantity = newQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }
}
