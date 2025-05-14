package com.example.retofit2.dto.responseDTO;

public class CartItemUpdateResultDTO {
    private int cartItemId;
    private int updatedQuantity;
    private boolean outOfStock;
    private String message;

    public CartItemUpdateResultDTO(int cartItemId, int updatedQuantity, boolean outOfStock, String message) {
        this.cartItemId = cartItemId;
        this.updatedQuantity = updatedQuantity;
        this.outOfStock = outOfStock;
        this.message = message;
    }


    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getUpdatedQuantity() {
        return updatedQuantity;
    }

    public void setUpdatedQuantity(int updatedQuantity) {
        this.updatedQuantity = updatedQuantity;
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
