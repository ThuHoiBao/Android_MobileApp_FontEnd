package com.example.retofit2.dto.requestDTO;

public class AddressDelivery {
    private String name;
    private String phone;
    private String details;
    private boolean isDefault;

    public AddressDelivery(String name, String phone, String details, boolean isDefault) {
        this.name = name;
        this.phone = phone;
        this.details = details;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
