package com.example.retofit2.api;

import com.example.retofit2.dto.requestDTO.ProductRequestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductAPI {
    @GET("products")  // Đảm bảo endpoint này khớp với API của bạn
    Call<List<ProductRequestDTO>> getAllProducts();
}
