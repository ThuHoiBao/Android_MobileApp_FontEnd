package com.example.retofit2.api;

import com.example.retofit2.dto.requestDTO.AddProductToCartRequestDTO;
import com.example.retofit2.dto.requestDTO.CardItemDTO;
import com.example.retofit2.dto.requestDTO.CartItemUpdateRequestDTO;
import com.example.retofit2.dto.responseDTO.CartItemUpdateResultDTO;
import com.example.retofit2.dto.responseDTO.ResponseObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ICartAPI {
    @GET("cart/items")
    Call<List<CardItemDTO>> getCartItems( @Query("userId") long userId);

    @PUT("cart/update")
    Call<List<CartItemUpdateResultDTO>> updateCartItems(
            @Body List<CartItemUpdateRequestDTO> updates);

    @POST("cart/add")
    Call<ResponseObject> addProductToCart(
            @Body AddProductToCartRequestDTO request,
            @Query("userId") long userId
    );

    @DELETE("cart/item")
    Call<String> removeCartItem(@Query("cartItemId") int cardItemId);
}
