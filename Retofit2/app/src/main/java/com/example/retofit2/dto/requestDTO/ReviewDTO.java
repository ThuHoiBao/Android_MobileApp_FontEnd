package com.example.retofit2.dto.requestDTO;

import java.util.List;

public class ReviewDTO {
    private String userName;
    private int ratingValue;
    private String comment;
    private String reviewDate;
    private List<String> imageReviews;  // Danh sách URL hoặc đường dẫn hình ảnh

    // Constructor, getter, and setter methods
    public ReviewDTO(String userName, int ratingValue, String comment, String reviewDate, List<String> imageReviews) {
        this.userName = userName;
        this.ratingValue = ratingValue;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.imageReviews = imageReviews;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public List<String> getImageReviews() {
        return imageReviews;
    }

    public void setImageReviews(List<String> imageReviews) {
        this.imageReviews = imageReviews;
    }
}
