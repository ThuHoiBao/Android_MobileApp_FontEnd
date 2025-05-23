package com.example.retofit2.api.retrofit;

import com.example.retofit2.api.CategoryAPI;
import com.example.retofit2.api.ProductAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryRetrofit {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/";


    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static CategoryAPI getApiService() {
        return getRetrofitInstance().create(CategoryAPI.class);
    }
}