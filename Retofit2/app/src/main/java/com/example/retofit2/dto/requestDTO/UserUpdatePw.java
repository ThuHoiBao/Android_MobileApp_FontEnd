package com.example.retofit2.dto.requestDTO;

import com.google.gson.annotations.SerializedName;

public class UserUpdatePw {
    private String email;

    @SerializedName("new_password")
    private String newPassword;

    public UserUpdatePw() {
    }

    public UserUpdatePw(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "UserUpdatePw{" +
                "email='" + email + '\'' +
                ", new_password='" + newPassword + '\'' +
                '}';
    }
}
