package com.example.retofit2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import com.example.retofit2.R;
import com.example.retofit2.Toast.CustomToast;
import com.example.retofit2.api.UserAPI;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.UserUpdatePw;
import com.example.retofit2.utils.SharedPrefManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText passwordET, cofirmPasswordET;
    private AppCompatButton updateBtn;
    private boolean passwordShowing = false;
    private boolean confirmPasswordShowing = false;
    private String password;
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.new_credentials);

        context = UpdatePasswordActivity.this;
        passwordET = findViewById(R.id.passwordET);
        cofirmPasswordET = findViewById(R.id.forgetPasswordET);
        ImageView passwordIcon = findViewById(R.id.passwordIcon);
        ImageView  confirmPasswordIcon = findViewById(R.id.confirmPasswordICon);
        updateBtn = findViewById(R.id.updateBtn);
        final String email = getIntent().getStringExtra("email");
        passwordIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(passwordShowing){
                    passwordShowing = false;
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_show);
                }else {
                    passwordShowing = true;
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_hide);
                }
                passwordET.setSelection(passwordET.length());
            }
        });

        confirmPasswordIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(confirmPasswordShowing){
                    confirmPasswordShowing = false;
                    cofirmPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmPasswordIcon.setImageResource(R.drawable.password_show);
                }else {
                    passwordShowing = true;
                    cofirmPasswordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirmPasswordIcon.setImageResource(R.drawable.password_hide);
                }
                cofirmPasswordET.setSelection(passwordET.length());
            }
        });


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = passwordET.getText().toString().trim();
                String newPassword = cofirmPasswordET.getText().toString().trim();
                if(password.isEmpty() || newPassword.isEmpty()){
                    CustomToast.makeText(UpdatePasswordActivity.this, "Password or ConfirmPassWord is not empty!", CustomToast.LONG, CustomToast.WARNING, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle the click event here
                        }
                    }).show();
                } else if(!password.equals(newPassword)){
                    CustomToast.makeText(UpdatePasswordActivity.this, "Password and password confirmation must be the same", CustomToast.LONG, CustomToast.WARNING, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle the click event here
                        }
                    }).show();
                }
                UserUpdatePw userUpdatePw = new UserUpdatePw(email, password);
                String json = new Gson().toJson(userUpdatePw);
                Log.d("Gson", "UserUpdatePw object as JSON: " + json);
                callUpdatePasswordApi(userUpdatePw);
            }
        });
    }

    private void callUpdatePasswordApi(UserUpdatePw userUpdatePw){
        UserAPI userAPI = APIRetrofit.getRetrofitInstance().create(UserAPI.class);
        Call<String> call = userAPI.updatePassword(userUpdatePw);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){

                    //Remove data SharedPreferences
                    SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
                    sharedPrefManager.clearSharedPreferences();

                    Intent intent = new Intent(UpdatePasswordActivity.this, UpdatePasswordSuccess.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("UpdatePasswordApi", "Failed to update password. Response code: " + response.code() + ", Message: " + response.message());
                    CustomToast.makeText(UpdatePasswordActivity.this, "Updated password is failed!", CustomToast.LONG, CustomToast.ERROR, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle the click event here
                        }
                    }).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("UpdatePasswordApi", "Error updating password: " + t.getMessage());
                t.printStackTrace();
                CustomToast.makeText(UpdatePasswordActivity.this, "Updated password is failed!", CustomToast.LONG, CustomToast.ERROR, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle the click event here
                    }
                }).show();
            }
        });
    }
}
