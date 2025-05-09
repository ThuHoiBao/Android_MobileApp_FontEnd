package com.example.retofit2.dto.requestDTO;

public class AddProductToCartRequestDTO {
    private String product_name;
    private String color;
    private int quantity;

    public AddProductToCartRequestDTO() {
    }

    public AddProductToCartRequestDTO(String product_name, String color, int quantity) {
        this.product_name = product_name;
        this.color = color;
        this.quantity = quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
