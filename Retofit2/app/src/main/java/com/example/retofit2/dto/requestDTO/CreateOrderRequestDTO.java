package com.example.retofit2.dto.requestDTO;

import java.util.List;

public class CreateOrderRequestDTO {
    private long userId;
    private List<Integer> cartItemIds;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<Integer> getCartItemIds() {
        return cartItemIds;
    }

    public void setCartItemIds(List<Integer> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public CreateOrderRequestDTO() {
    }

    public CreateOrderRequestDTO(long userId, List<Integer> cartItemIds) {
        this.userId = userId;
        this.cartItemIds = cartItemIds;
    }
}
