package com.example.retofit2.api;

import com.example.retofit2.dto.requestDTO.PaymentRequestDTO;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface VNPayAPI {
    @POST("v1/payments/create_payment_url")
    Call<String> createPaymentUrl(@Body PaymentRequestDTO paymentRequest);

    @GET("v1/payments/vnpay/return")
    Call<String> handleVnPayReturn(@QueryMap Map<String, String> queryParams);
}
