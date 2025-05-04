package com.example.retofit2.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.retofit2.R;
import com.example.retofit2.Toast.CustomToast;
import com.example.retofit2.api.UserAPI;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.UserDTO;
import com.example.retofit2.utils.OtpCallback;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPVerification extends AppCompatActivity {
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() > 0){
                if(selectecPosition == 0){
                    selectecPosition = 1;
                    showKeyboard(otpEt2);
                } else if(selectecPosition == 1){
                    selectecPosition = 2;
                    showKeyboard(otpEt3);
                } else if (selectecPosition == 2){
                    selectecPosition = 3;
                    showKeyboard(otpEt4);
                } else if(selectecPosition == 3){
                    selectecPosition = 4;
                    showKeyboard(otpEt5);
                } else if(selectecPosition == 4){
                    selectecPosition = 5;
                    showKeyboard(otpEt6);
                }
            }
        }
    };

    private EditText otpEt1, otpEt2,otpEt3, otpEt4, otpEt5, otpEt6;
    private TextView resendBtn;

    //true after every 60 seconds
    private boolean resendEnaled = false;

    //resend time in seconds;
    private int resendTime = 60;
    private int selectecPosition = 0;
    private String getEmail;
    private String OTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpverification);

        otpEt1 = findViewById(R.id.otpET1);
        otpEt2 = findViewById(R.id.otpET2);
        otpEt3 = findViewById(R.id.otpET3);
        otpEt4 = findViewById(R.id.otpET4);
        otpEt5 = findViewById(R.id.otpET5);
        otpEt6 = findViewById(R.id.otpET6);

        resendBtn = findViewById(R.id.resendBtn);
        final Button verifyBtn = findViewById(R.id.verifyBtn);

        final TextView otpEmail = findViewById(R.id.otpEmail);

        //getting email and mobile Register activity through intent
        getEmail = getIntent().getStringExtra("email");
        if(getEmail != null){
            callApiSendOTP(getEmail, new OtpCallback() {
                @Override
                public void onOtpReceived(String otp) {
                    OTP = otp;
                }
            });
        }
        //setting email and mobile to TextView
        otpEmail.setText(getEmail);

        otpEt1.addTextChangedListener(textWatcher);
        otpEt2.addTextChangedListener(textWatcher);
        otpEt3.addTextChangedListener(textWatcher);
        otpEt4.addTextChangedListener(textWatcher);
        otpEt5.addTextChangedListener(textWatcher);
        otpEt6.addTextChangedListener(textWatcher);

        // by default open key keyboard at otpEt1
        showKeyboard(otpEt1);

        //start resend count doww timer
        startCountDownTimer();

        resendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(resendEnaled){
                    //handle your resend code here
                    if(getEmail != null){
                        callApiSendOTP(getEmail, new OtpCallback() {
                            @Override
                            public void onOtpReceived(String otp) {
                                OTP = otp;
                            }
                        });
                    }
                    //start new resend count down timer
                    startCountDownTimer();
                }
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final String generateOtp =
                              otpEt1.getText().toString()
                            + otpEt2.getText().toString()
                            + otpEt3.getText().toString()
                            + otpEt4.getText().toString()
                            + otpEt5.getText().toString()
                            + otpEt6.getText().toString();


                if(generateOtp.length() == 6){
                    //handle your otp verification here
                    if(getEmail != null && generateOtp.equals(OTP) && Objects.requireNonNull(getIntent().getStringExtra("flag")).equalsIgnoreCase("ForgetPassword")){
                        Intent intent = new Intent(OTPVerification.this, UpdatePasswordActivity.class);
                        intent.putExtra("email", getEmail);
                        startActivity(intent);
                        finish();
                    } else if (getEmail != null && generateOtp.equals(OTP) && Objects.equals(getIntent().getStringExtra("flag"), "Register")){
                        //call API register
                        UserDTO userDTO = (UserDTO) getIntent().getSerializableExtra("userDTO");
                        callSignUpApi(userDTO);
                        //Register sucess
                    }else {
                        CustomToast.makeText(OTPVerification.this, "OTP is not valid!", CustomToast.LONG, CustomToast.WARNING, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle the click event here
                            }
                        }).show();
                    }
                }
            }
        });
    }

    private void showKeyboard(EditText otpET){
        otpET.requestFocus();
        InputMethodManager inputMathodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMathodManager.showSoftInput(otpET, InputMethodManager.SHOW_IMPLICIT);
    }


    private void startCountDownTimer(){
        resendEnaled = false;
        resendBtn.setTextColor(Color.parseColor("#99000000"));
        new CountDownTimer(resendTime * 1000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                resendBtn.setText("Resend Code (" + (millisUntilFinished / 1000) + ")");
            }

            @Override
            public void onFinish() {
                resendEnaled = true;
                resendBtn.setText("Resend Code");
                resendBtn.setTextColor(getResources().getColor((R.color.primary)));
            }
        }.start();

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DEL){
            if(selectecPosition == 5){
                selectecPosition = 4;
                showKeyboard(otpEt5);
            }
            if(selectecPosition == 4){
                selectecPosition = 3;
                showKeyboard(otpEt4);
            } else if (selectecPosition == 3){
                selectecPosition = 2;
                showKeyboard(otpEt3);
            } else if(selectecPosition == 2){
                selectecPosition = 1;
                showKeyboard(otpEt2);
            } else if(selectecPosition == 1){
                selectecPosition = 0;
                showKeyboard(otpEt1);
            }
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    private void callApiSendOTP(String to, OtpCallback otpCallback){
        UserAPI userAPI = APIRetrofit.getRetrofitInstance().create(UserAPI.class);
        Call<String> call = userAPI.sendOtp(to);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<
                    String> response) {
                if(response.isSuccessful()){
                    String otp = response.body();
                    otpCallback.onOtpReceived(otp);
                } else {
                    CustomToast.makeText(OTPVerification.this, "Failed to send OTP!", CustomToast.LONG, CustomToast.ERROR, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle the click event here
                        }
                    }).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(OTPVerification.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callSignUpApi(UserDTO userDTO){
        UserAPI userAPI = APIRetrofit.getRetrofitInstance().create(UserAPI.class);
        Call<UserDTO> call = userAPI.register(userDTO);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(OTPVerification.this, SignInSuccessful.class);
                    startActivity(intent);
                    finish();
                } else {
                    CustomToast.makeText(OTPVerification.this, "Sign up is failed", CustomToast.LONG, CustomToast.ERROR, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle the click event here
                        }
                    }).show();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(OTPVerification.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}