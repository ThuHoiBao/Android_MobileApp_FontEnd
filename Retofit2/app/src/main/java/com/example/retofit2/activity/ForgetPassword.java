package com.example.retofit2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import com.example.retofit2.R;
import com.example.retofit2.Toast.CustomToast;
import com.example.retofit2.api.UserAPI;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.UserDTO;
import com.example.retofit2.utils.EmailValidator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {
    private AppCompatButton btnNext;
    private EditText emailET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forget_password);

        btnNext = findViewById(R.id.nextBtn);
        emailET = findViewById(R.id.editTextEmail);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                if(email.isEmpty()){
                    CustomToast.makeText(ForgetPassword.this, "Email is not empty!", CustomToast.LONG, CustomToast.WARNING, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                } else if(!EmailValidator.isValidEmail(email)){
                    CustomToast.makeText(ForgetPassword.this, "Email is not in correct format!", CustomToast.LONG, CustomToast.WARNING, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                }
                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(email);
                callCheckEmailApi(userDTO);
            }
        });
    }

    private void callCheckEmailApi(UserDTO userDTO){
        UserAPI userAPI = APIRetrofit.getRetrofitInstance().create(UserAPI.class);
        Call<String> call = userAPI.verifyEmail(userDTO.getEmail());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(ForgetPassword.this, OTPVerification.class);
                    intent.putExtra("email",userDTO.getEmail());
                    intent.putExtra("flag", "ForgetPassword");
                    startActivity(intent);
                    finish();
                } else {
                    CustomToast.makeText(ForgetPassword.this, "Email is not exits!", CustomToast.LONG, CustomToast.WARNING, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle the click event here
                        }
                    }).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Network error: ", t.getMessage());
                Toast.makeText(ForgetPassword.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
