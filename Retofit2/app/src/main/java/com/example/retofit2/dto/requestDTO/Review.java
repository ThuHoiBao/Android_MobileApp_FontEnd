package com.example.retofit2.dto.requestDTO;

import java.util.List;


public class Review {
    private String userName;
    private float rating;
    private String reviewContent;
    private String reviewDate;
    private List<String> reviewImages;  // Danh sách URL hoặc đường dẫn hình ảnh

    // Constructor, getter, and setter methods
    public Review(String userName, float rating, String reviewContent, String reviewDate, List<String> reviewImages) {
        this.userName = userName;
        this.rating = rating;
        this.reviewContent = reviewContent;
        this.reviewDate = reviewDate;
        this.reviewImages = reviewImages;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public List<String> getReviewImages() {
        return reviewImages;
    }

    public void setReviewImages(List<String> reviewImages) {
        this.reviewImages = reviewImages;
    }
}
