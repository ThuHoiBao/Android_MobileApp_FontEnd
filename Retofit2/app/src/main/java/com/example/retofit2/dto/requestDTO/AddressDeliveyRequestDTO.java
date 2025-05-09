package com.example.retofit2.dto.requestDTO;

public class AddressDeliveyRequestDTO {
    private String address;
    private String phone_number;
    private String full_name;
    private Boolean is_default;
    private Long user_id;
    private Boolean status;

    public AddressDeliveyRequestDTO() {
    }

    public AddressDeliveyRequestDTO(String address, String phoneNumber, String fullName, Boolean isDefault, Long userId, Boolean status) {
        this.address = address;
        this.phone_number = phoneNumber;
        this.full_name = fullName;
        this.is_default = isDefault;
        this.user_id = userId;
        this.status = status;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone_number = phoneNumber;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String fullName) {
        this.full_name = fullName;
    }

    public Boolean getDefault() {
        return is_default;
    }

    public void setDefault(Boolean aDefault) {
        is_default = aDefault;
    }

    public Long getUserId() {
        return user_id;
    }

    public void setUserId(Long userId) {
        this.user_id = userId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
