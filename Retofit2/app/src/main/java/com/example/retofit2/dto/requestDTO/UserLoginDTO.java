package com.example.retofit2.dto.requestDTO;

import com.google.gson.annotations.SerializedName;

public class UserLoginDTO {
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("password")
    private String password;

    public UserLoginDTO(String phone_number, String password) {
        this.phone_number = phone_number;
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

