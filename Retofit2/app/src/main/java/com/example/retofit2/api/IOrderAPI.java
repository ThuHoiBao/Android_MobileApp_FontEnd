package com.example.retofit2.api;

import com.example.retofit2.dto.requestDTO.CreateOrderRequestDTO;
import com.example.retofit2.dto.responseDTO.OrderResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IOrderAPI {
    @POST("orders/from-cart")
    Call<OrderResponseDTO> createOrderFromCart(@Body CreateOrderRequestDTO requestDTO);
}
