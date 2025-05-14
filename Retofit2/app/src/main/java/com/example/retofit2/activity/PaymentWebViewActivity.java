package com.example.retofit2.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.retofit2.R;
import com.example.retofit2.api.VNPayAPI;
import com.example.retofit2.api.retrofit.APIRetrofit;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PaymentWebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_webview);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return handleUrl(request.getUrl().toString());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleUrl(url);
            }

            private boolean handleUrl(String url) {
                if (url.startsWith("myapp://vnpay_callback")) {
                    Uri data = Uri.parse(url);
                    Toast.makeText(PaymentWebViewActivity.this, "Đang xử lý kết quả thanh toán...", Toast.LENGTH_SHORT).show();
                    handleVnPayReturn(data);
                    return true;
                }

                if (url.startsWith("http://10.0.126.156:4200/payments/payment-callback")) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("payment_status", "success");
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                    return true;
                }

                return false;
            }
        });

        String paymentUrl = getIntent().getStringExtra("paymentUrl");
        if (paymentUrl != null) {
            webView.loadUrl(paymentUrl);
        }
    }



    private void handleVnPayReturn(Uri data){
        // Chuyển tất cả query parameters thành Map
        Map<String, String> queryParams = new HashMap<>();
        for (String paramName : data.getQueryParameterNames()) {
            queryParams.put(paramName, data.getQueryParameter(paramName));
        }

        // Gọi API xử lý thanh toán trên server bằng Retrofit
        VNPayAPI api = APIRetrofit.getVNPAYApiService();
        Call<String> call = api.handleVnPayReturn(queryParams);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String transactionStatus = queryParams.get("vnp_TransactionStatus");
                    if ("00".equals(transactionStatus)) {
                        // Giao dịch thành công
                        Toast.makeText(PaymentWebViewActivity.this, "Thanh toán thành công", Toast.LENGTH_LONG).show();
                        // Chuyển hướng đến trang đơn hàng
                        startActivity(new Intent(PaymentWebViewActivity.this, OrderActivity.class));
                    } else {
                        // Giao dịch thất bại
                        Toast.makeText(PaymentWebViewActivity.this, "Thanh toán thất bại", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(PaymentWebViewActivity.this, "Xác thực thất bại", Toast.LENGTH_LONG).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(PaymentWebViewActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}
