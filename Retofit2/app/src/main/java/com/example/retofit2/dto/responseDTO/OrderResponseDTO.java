package com.example.retofit2.dto.responseDTO;

import java.util.Date;

public class OrderResponseDTO {
    private int orderId;

    private Long userId;

    private AddressDeliveryDTO addressDelivery;

    private String orderStatus;

    private Date orderDate;

    public OrderResponseDTO() {
    }

    public OrderResponseDTO(int orderId, Long userId, AddressDeliveryDTO addressDelivery, String orderStatus, Date orderDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.addressDelivery = addressDelivery;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AddressDeliveryDTO getAddressDelivery() {
        return addressDelivery;
    }

    public void setAddressDelivery(AddressDeliveryDTO addressDelivery) {
        this.addressDelivery = addressDelivery;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
