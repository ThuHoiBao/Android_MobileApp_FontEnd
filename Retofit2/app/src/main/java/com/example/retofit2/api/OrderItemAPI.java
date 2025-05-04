package com.example.retofit2.api;

import com.example.retofit2.dto.requestDTO.OrderItemRequestDTO;
import com.example.retofit2.dto.requestDTO.ProductRequestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderItemAPI {
    @GET("orders/{customerId}")
    Call<List<OrderItemRequestDTO>> getAllOrderItem(@Path("customerId") int customerId);

    @GET("orders/{customerId}/{orderStatus}")
    Call<List<OrderItemRequestDTO>> getOrderStatusItem(@Path("customerId") int customerId,
                                                       @Path("orderStatus") String orderStatus);

    @GET("orders/{orderItemId}/{orderStatus}")
    Call<List<OrderItemRequestDTO>> updateOrderStatusItem(@Path("orderItemId") int orderItemId,
                                                       @Path("orderStatus") String orderStatus);

    @PUT("orders/cancel/{orderId}")
    Call<Void> cancelOrder(@Path("orderId") int orderId);
}
