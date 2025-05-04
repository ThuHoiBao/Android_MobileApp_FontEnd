package com.example.retofit2.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadImageAPI {

    @Multipart
    @POST("uploadImage")
    Call<ResponseBody> uploadImage(
            @Part MultipartBody.Part file,
            @Part("productName") String productName
    );
//video va anh
    @Multipart
    @POST("uploadMedia")
    Call<ResponseBody> uploadMedia(
            @Part MultipartBody.Part file,
            @Part("productName") String productName
    );

}
