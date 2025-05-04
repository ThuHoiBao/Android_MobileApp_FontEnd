package com.example.retofit2.api;

import com.example.retofit2.dto.requestDTO.CategoryRequestDTO;
import com.example.retofit2.dto.requestDTO.ProductRequestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryAPI {
    @GET("categories")
    Call<List<CategoryRequestDTO>> getAllCategories();

    @GET("categories/{categoryId}")
    Call<List<ProductRequestDTO>> getPhonesByCategory(@Path("categoryId") int categoryId);

}
