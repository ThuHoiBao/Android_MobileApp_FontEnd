package com.example.retofit2.dto.requestDTO;

public class PaymentRequestDTO {
    private int order_id;
    private long amount; // Số tiền cần thanh toán
    private String bankCode;  // Mã ngân hàng
    private String language; // Ngôn ngữ giao diện thanh toán (vd: "vn", "en")

    public PaymentRequestDTO() {
    }

    public PaymentRequestDTO(int order_id, long amount, String bankCode, String language) {
        this.order_id = order_id;
        this.amount = amount;
        this.bankCode = bankCode;
        this.language = language;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
