package com.example.retofit2.dto.responseDTO;

public class AddressDeliveryDTO {
    private Long id;
    private String address;
    private String phoneNumber;
    private String fullName;
    private Boolean isDefault;
    private Long userId;
    private Boolean status;

    public AddressDeliveryDTO() {
    }

    public AddressDeliveryDTO(Long id , String fullName, String phoneNumber, String address, Boolean isDefault, long userId, boolean status) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isDefault = isDefault;
        this.userId = userId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
