package com.example.retofit2.api;

import com.example.retofit2.dto.requestDTO.CardItemDTO;
import com.example.retofit2.dto.requestDTO.CreateOrderRequestDTO;
import com.example.retofit2.dto.requestDTO.OrderItemDTO;
import com.example.retofit2.dto.responseDTO.OrderResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IOrderAPI {
    @POST("orders/from-cart")
    Call<OrderResponseDTO> createOrderFromCart(@Body CreateOrderRequestDTO requestDTO);

    @GET("orders/items")
    Call<List<CardItemDTO>> getOrderItems(@Query("orderId") int orderId);
}
