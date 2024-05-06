package com.example.chatbot;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatbot.API.RetrofitClient;
import com.example.chatbot.API.models.ResponsePost;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_USERS_NAME = "extra_users_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_USERS_NAME)) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        String username = intent.getStringExtra(EXTRA_USERS_NAME);
        String message = "todo";

        Call<ResponsePost> call = RetrofitClient.getInstance()
                .getAPI().getChatResponse(username, message);

        call.enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Error occurred!");
                    return;
                }

                String messageResponse = response.body().message;


//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable throwable) {
                System.out.println("BAD ERROR" + throwable.getMessage());
            }
        });

    }
}