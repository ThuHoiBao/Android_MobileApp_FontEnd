package com.example.retofit2.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retofit2.R;
import com.example.retofit2.adapter.customer.MessageAdapter;
import com.example.retofit2.dto.responseDTO.Message;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatBoxActivity  extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> messageList = new ArrayList<>();
    private EditText editText;
    private ImageView backIcon;
    private Button buttonSend;
    private GenerativeModelFutures model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbox_ai);

        recyclerView = findViewById(R.id.recycler_view);
        editText = findViewById(R.id.edit_text);
        buttonSend = findViewById(R.id.button_send);
        backIcon = findViewById(R.id.back_button);

        adapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatBoxActivity.this, CustomerHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        GenerativeModel gm = new GenerativeModel(
                "tunedModels/mobilephoneandroid-5g9kpw1m6g5bpu3ykqwj7", // Replace with your tuned model ID
                "AIzaSyBPlmczytElD-5zMgornoEpZSJ3jKL4J0k" // Replace with your API key
        );
        model = GenerativeModelFutures.from(gm);


        buttonSend.setOnClickListener(v -> {
            String query = editText.getText().toString().trim();
            if (!query.isEmpty()) {
                getResponse(query);
                messageList.add(new Message(query, true, getCurrentTime()));
                adapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
                editText.setText("");
            }
        });
    }


    private String getCurrentTime() {
        return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
    }

    private void getResponse(String query) {
        // Create a Content object with the user's query
        Content content = new Content.Builder()
                .addText(query)
                .build();

        // Pass the Content object to generateContent
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String resultText = result.getText();
                    runOnUiThread(() -> {
                        messageList.add(new Message(resultText, false, getCurrentTime()));
                        adapter.notifyItemInserted(messageList.size() - 1);
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    });
                }

                @Override
                public void onFailure(Throwable t) {
                    runOnUiThread(() -> {
                        messageList.add(new Message("Error: " + t.getMessage(), false, getCurrentTime()));
                        adapter.notifyItemInserted(messageList.size() - 1);
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    });
                }
            }, getMainExecutor());
        }
    }

}
