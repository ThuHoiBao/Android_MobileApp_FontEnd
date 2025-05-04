package com.example.retofit2.dto.requestDTO;

public class PaymentInfo {
    private String address;
    private String totalAmount;

    // Constructor
    public PaymentInfo(String address, String totalAmount) {
        this.address = address;
        this.totalAmount = totalAmount;
    }

    // Getters
    public String getAddress() {
        return address;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
