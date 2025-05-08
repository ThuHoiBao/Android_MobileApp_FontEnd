package com.example.retofit2.dto.requestDTO;

public class CardItemDTO {
    private int cartItemId;
    private String productName;
    private String color;
    private double price;
    private int quantity;
    private String productImage;
    private boolean isSelected = false;
    private boolean outOfStockItems;

    public CardItemDTO(int cartItemId,String productName, String color, double price, int quantity, String productImage, boolean outOfStockItems) {
        this.cartItemId = cartItemId;
        this.productName = productName;
        this.color = color;
        this.price = price;
        this.quantity = quantity;
        this.productImage = productImage;
        this.outOfStockItems = outOfStockItems;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isOutOfStockItems() {
        return outOfStockItems;
    }

    public void setOutOfStockItems(boolean outOfStockItems) {
        this.outOfStockItems = outOfStockItems;
    }
}
