package com.example.retofit2.dto.requestDTO;

import com.google.gson.annotations.SerializedName;

public class CartItemUpdateRequestDTO {
    @SerializedName("cartItem_Id")
    private int cartItemId;
    @SerializedName("product_name")
    private String productName;

    @SerializedName("color")
    private String color;

    @SerializedName("new_quantity")
    private int newQuantity;

    // Constructor, getters và setters
    public CartItemUpdateRequestDTO(int cartItemId,String productName, String color, int newQuantity) {
        this.cartItemId = cartItemId;
        this.productName = productName;
        this.color = color;
        this.newQuantity = newQuantity;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
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
