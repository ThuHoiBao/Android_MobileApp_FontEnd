package com.example.retofit2.utils;

import com.example.retofit2.dto.responseDTO.OrderResponseDTO;

public interface OrderCallback {
    void onSuccess(OrderResponseDTO orderResponseDTO);
    void onError(Throwable t);
}
