package com.example.chatbot;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessagesViewHolder> {

    public ArrayList<ChatMessage> chatMessages;

    private Context context;

    public ChatMessageAdapter(Context context, ArrayList<ChatMessage> items) {
        this.chatMessages = items;
        this.context = context;
    }


    @NonNull
    @Override
    public ChatMessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_message, parent, false);
        return new ChatMessagesViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageAdapter.ChatMessagesViewHolder holder, int position) {
        ChatMessage ChatMessage = chatMessages.get(position);
        holder.bind(ChatMessage);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class ChatMessagesViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llMessageContainer;
        private ImageView tvAiIcon;
//        private Button tvUsersIcon;
        private TextView tvMessageText;
        private ImageView gifSpinner;

        private Context context;

        public ChatMessagesViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            llMessageContainer = itemView.findViewById(R.id.llMessageContainer);
            tvAiIcon = itemView.findViewById(R.id.tvAiIcon);
//            tvUsersIcon = itemView.findViewById(R.id.tvUsersIcon);
            tvMessageText = itemView.findViewById(R.id.tvMessageText);
            gifSpinner = itemView.findViewById(R.id.gifSpinner);
            this.context = context;
        }

        @SuppressLint("ResourceAsColor")
        public void bind(ChatMessage chatMessage) {
//            tvUsersIcon.setVisibility(View.GONE);
            tvAiIcon.setVisibility(View.GONE);

            gifSpinner.setVisibility(View.GONE);
            tvMessageText.setText(chatMessage.getMessage());

            if (chatMessage.getAuthor() == ChatMessage.AUTHOR_TYPE.AUTHOR_TYPE_USER) {
//                tvUsersIcon.setVisibility(View.VISIBLE);
                llMessageContainer.setGravity(Gravity.RIGHT);
                tvMessageText.setBackgroundResource(R.drawable.text_view_background_user);
//                tvMessageText.setBackgroundColor(R.color.chat_user_bubble_background);
//                tvMessageText.setTextColor(R.color.chat_user_bubble_text);
            } else {
                tvAiIcon.setVisibility(View.VISIBLE);
                llMessageContainer.setGravity(Gravity.LEFT);
                tvMessageText.setBackgroundResource(R.drawable.text_view_background_ai);
                tvMessageText.setVisibility(View.VISIBLE);

                if(chatMessage.getMessage().isEmpty()){
                    gifSpinner.setVisibility(View.VISIBLE);
                    tvMessageText.setVisibility(View.GONE);
                }
//                tvMessageText.setBackgroundColor(R.color.chat_ai_bubble_background);
//                tvMessageText.setTextColor(R.color.chat_ai_bubble_text);
            }

        }

    }

}