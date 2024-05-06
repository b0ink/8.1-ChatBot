package com.example.chatbot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot.API.RetrofitClient;
import com.example.chatbot.API.models.ResponsePost;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_USERS_NAME = "extra_users_name";

    private ImageButton btnSend;
    private EditText etMessage;

    private RecyclerView recyclerView;

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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_USERS_NAME)) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        btnSend = findViewById(R.id.btnSend);
        etMessage = findViewById(R.id.etMessage);

        ArrayList<ChatMessage> chatMessages = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ChatMessageAdapter adapter = new ChatMessageAdapter(this, chatMessages);
        recyclerView.setAdapter(adapter);

        String username = intent.getStringExtra(EXTRA_USERS_NAME);

        etMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    btnSend.performClick();
                    btnSend.requestFocus();
                    return true;
                }
                return false;
            }
        });

        btnSend.setOnClickListener(view -> {
            if(chatMessages.size() > 0){
                ChatMessage lastMessage = chatMessages.get(chatMessages.size()-1);
                if(lastMessage.getAuthor() == ChatMessage.AUTHOR_TYPE.AUTHOR_TYPE_USER){
                    return;
                }
            }

            String message = etMessage.getText().toString();
            if (message.isEmpty()) {
                return;
            }

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            ChatMessage usersMessage = new ChatMessage(message, ChatMessage.AUTHOR_TYPE.AUTHOR_TYPE_USER);
            chatMessages.add(usersMessage);
            adapter.notifyItemInserted(chatMessages.size()-1);
            recyclerView.scrollToPosition(chatMessages.size()-1);
            System.out.println("inserting users message");
            etMessage.setText("");
            etMessage.clearFocus();
            etMessage.clearComposingText();
            etMessage.setEnabled(false);

            ChatMessage placeholderMessage = new ChatMessage("", ChatMessage.AUTHOR_TYPE.AUTHOR_TYPE_AI);
            chatMessages.add(placeholderMessage);
            adapter.notifyItemInserted(chatMessages.size()-1);
            recyclerView.scrollToPosition(chatMessages.size()-1);

            Call<ResponsePost> call = RetrofitClient.getInstance()
                    .getAPI().getChatResponse(username, message);

            call.enqueue(new Callback<ResponsePost>() {
                @Override
                public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                    if (!response.isSuccessful()) {
                        System.out.println("Error occurred!");
                        return;
                    }

//                    chatMessages.remove(chatMessages.size()-1);
//                    adapter.notifyDataSetChanged();

                    String messageResponse = response.body().message;
//                    ChatMessage newMessage = new ChatMessage(messageResponse, ChatMessage.AUTHOR_TYPE.AUTHOR_TYPE_AI);
                    ChatMessage newMessage = chatMessages.get(chatMessages.size()-1);
                    newMessage.setMessage(messageResponse);

//                    chatMessages.add(newMessage);
                    adapter.notifyItemChanged(chatMessages.size()-1);
                    recyclerView.scrollToPosition(chatMessages.size()-1);
                    etMessage.setEnabled(true);

                    System.out.println("response from ai"+ messageResponse);
                }

                @Override
                public void onFailure(Call<ResponsePost> call, Throwable throwable) {
                    System.out.println("Error getting ai response" + throwable.getMessage());
                }
            });
        });


    }
}