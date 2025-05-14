package com.example.retofit2.api.retrofit;

import com.example.retofit2.api.IAddressDelivery;
import com.example.retofit2.api.ICartAPI;
import com.example.retofit2.api.IOrderAPI;
import com.example.retofit2.api.OrderItemAPI;
import com.example.retofit2.api.ProductAPI;
import com.example.retofit2.api.ReviewAPI;
import com.example.retofit2.api.UserAPI;
import com.example.retofit2.api.VNPayAPI;
import com.example.retofit2.authentication.AuthInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIRetrofit {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/"; // Thay bằng URL thật của backend
//  public static final String BASE_URL = "http://192.168.1.4:8080/api/"; // Thay bằng URL thật của backend

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())  // Sử dụng Gson để parse JSON
                    .build();
        }
        return retrofit;
    }

    //Ham nay duoc su dung de goi các API can Bearer Token
    public static Retrofit getInstance(String token) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(token))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static UserAPI getUserApiService(){
        return getRetrofitInstance().create(UserAPI.class);
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

    public static ICartAPI getCartAPIService(String token) {
        return getRetrofitInstance().create(ICartAPI.class);
    }

    public static IAddressDelivery getAddressDelivery(){
        return getRetrofitInstance().create(IAddressDelivery.class);
    }


    public static VNPayAPI getVNPAYApiService(){
        return getRetrofitInstance().create(VNPayAPI.class);
    }

    public static IOrderAPI getOrderApiService(){
        return getRetrofitInstance().create(IOrderAPI.class);
    }

}
