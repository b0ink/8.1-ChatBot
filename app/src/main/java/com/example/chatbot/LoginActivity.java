package com.example.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private Button btnStartTalking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        etUsername = findViewById(R.id.etUsername);
        btnStartTalking = findViewById(R.id.btnStartTalking);

        btnStartTalking.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            if(username.isEmpty()){
                Toast.makeText(this, "Username can not be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            Call<ResponsePost> call = RetrofitClient.getInstance()
                    .getAPI().initUser(username);

            call.enqueue(new Callback<ResponsePost>() {
                @Override
                public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                    if (!response.isSuccessful()) {
                        System.out.println("Error occurred!");
                        Toast.makeText(LoginActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.EXTRA_USERS_NAME, username);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<ResponsePost> call, Throwable throwable) {
                    System.out.println("InitUser error:" + throwable.getMessage());
                }
            });
        });
    }
}