package com.example.retofit2.api;

import com.example.retofit2.dto.requestDTO.ReviewRequestDTO;
import com.example.retofit2.dto.responseDTO.ReviewResponseDTO;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ReviewAPI {

    @Multipart
    @POST("review")
    Call<ResponseBody> uploadReview(
            @Part("review") RequestBody reviewJson,
            @Part List<MultipartBody.Part> images
    );

    @GET("reviews/{orderItemId}")
    Call<Boolean> checkReviewExists(@Path("orderItemId") int orderItemId);

    @GET("review/{orderItemId}")
    Call<ReviewRequestDTO> getReviewByOrderItemId(@Path("orderItemId") int orderItemId);


}
