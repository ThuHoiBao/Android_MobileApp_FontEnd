package com.example.retofit2.api.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadImageClientAPI {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.1.2:8080/api/";  // Cập nhật URL của backend

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
