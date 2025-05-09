package com.example.retofit2.api;

import com.example.retofit2.dto.requestDTO.AddressDeliveyRequestDTO;
import com.example.retofit2.dto.responseDTO.AddressDeliveryDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IAddressDelivery {
    @GET("address/default")
    Call<AddressDeliveryDTO> getDefaultAddress(@Query("userId") long userId);

    @GET("address/items")
    Call<List<AddressDeliveryDTO>> getAllAddressByUserId(@Query("userId") long userId);

    @PUT("address/setDefault")
    Call<String> setDefaultAddress(@Query("userId") long userId, @Query("addressId") long addressId);

    @POST("address/add")
    Call<AddressDeliveryDTO> createAddress(@Body AddressDeliveyRequestDTO dto);
}
