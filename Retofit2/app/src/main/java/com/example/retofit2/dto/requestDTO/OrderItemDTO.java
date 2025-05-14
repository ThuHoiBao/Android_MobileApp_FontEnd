package com.example.retofit2.dto.requestDTO;

public class OrderItemDTO {
    private String productName;
    private String color;
    private double price;
    private int quantity;

    public OrderItemDTO(String productName, String color, double price, int quantity) {
        this.productName = productName;
        this.color = color;
        this.price = price;
        this.quantity = quantity;
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
}
