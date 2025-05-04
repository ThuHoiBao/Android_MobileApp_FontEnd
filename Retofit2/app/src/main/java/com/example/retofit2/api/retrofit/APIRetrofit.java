package com.example.retofit2.api.retrofit;

import com.example.retofit2.api.OrderItemAPI;
import com.example.retofit2.api.ProductAPI;
import com.example.retofit2.api.ReviewAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRetrofit {

    private static final String BASE_URL = "http://192.168.1.195:8080/api/"; // Thay bằng URL thật của backend

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())  // Sử dụng Gson để parse JSON
                    .build();
        }
        return retrofit;
    }

    public static ProductAPI getApiService() {
        return getRetrofitInstance().create(ProductAPI.class);
    }
    public static OrderItemAPI getOrderItemAPIService() {
        return getRetrofitInstance().create(OrderItemAPI.class);
    }
    public static ReviewAPI getReviewAPIService() {
        return getRetrofitInstance().create(ReviewAPI.class);
    }


}
