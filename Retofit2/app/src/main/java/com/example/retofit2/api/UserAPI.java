package com.example.retofit2.api;



import com.example.retofit2.dto.requestDTO.TokenDTO;
import com.example.retofit2.dto.requestDTO.UserDTO;
import com.example.retofit2.dto.requestDTO.UserLoginDTO;
import com.example.retofit2.dto.requestDTO.UserRequestDTO;
import com.example.retofit2.dto.requestDTO.UserUpdatePw;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserAPI {
    @GET("v1/users/verify-token")  // Đảm bảo endpoint này khớp với API của bạn
    Call<UserLoginDTO> verifyToken(@Header("Authorization") String token);

    @GET("v1/users/verify-email")
    Call<String> verifyEmail(@Query("email") String email);

    @GET("v1/users/verify-phone")
    Call<String> verifyPhone(@Query("phone") String phone);

    @POST("v1/users/login")
    Call<TokenDTO> login(@Body UserLoginDTO userLoginDTO);

    @POST("v1/users/register")
    Call<UserDTO> register(@Body UserDTO userDTO);

    @POST("v1/users/send-otp")
    Call<String> sendOtp(@Query("to") String to);

    @PUT("v1/users/update-password")
    Call<String> updatePassword(@Body UserUpdatePw userUpdatePw);

    @POST("v1/users/login-social")
    Call<TokenDTO> loginWithGoogle(@Body UserDTO userDTO);

    @GET("v1/users/profile/{userId}")
    Call <UserRequestDTO> getUserProfile (@Path("userId") int userId);

    @Multipart
    @POST("v1/users/profile/update")
    Call<ResponseBody> updateProfileUser(
            @Part("user") RequestBody userJson,
            @Part MultipartBody.Part imgProfile
    );
}
