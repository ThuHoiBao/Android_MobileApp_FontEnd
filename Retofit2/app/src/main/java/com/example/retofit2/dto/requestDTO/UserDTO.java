package com.example.retofit2.dto.requestDTO;

import java.io.Serializable;
import java.sql.Date;

public class UserDTO implements Serializable {
    private String fullname;
    private String phone_number;
    private String email;
    private String address;
    private String password;
    private String retype_password;
    private Date date_of_birth;
    private String facebook_account_id;
    private String google_account_id;
    private long role_id;
    private String profile_image;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetype_password() {
        return retype_password;
    }

    public void setRetype_password(String retype_password) {
        this.retype_password = retype_password;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getFacebook_account_id() {
        return facebook_account_id;
    }

    public void setFacebook_account_id(String facebook_account_id) {
        this.facebook_account_id = facebook_account_id;
    }

    public String getGoogle_account_id() {
        return google_account_id;
    }

    public void setGoogle_account_id(String google_account_id) {
        this.google_account_id = google_account_id;
    }

    public long getRole_id() {
        return role_id;
    }

    public void setRole_id(long role_id) {
        this.role_id = role_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
