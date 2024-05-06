package com.example.chatbot;


import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
        private RelativeLayout rlTaskView;

        private LinearLayout llMessageContainer;
        private Button tvAiIcon;
        private Button tvUsersIcon;
        private Button tvMessageText;


        private Context context;

        public ChatMessagesViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            llMessageContainer = itemView.findViewById(R.id.llMessageContainer);
            tvAiIcon = itemView.findViewById(R.id.tvAiIcon);
            tvUsersIcon = itemView.findViewById(R.id.tvUsersIcon);
            tvMessageText = itemView.findViewById(R.id.tvMessageText);

            this.context = context;
        }

        public void bind(ChatMessage chatMessage) {
            tvUsersIcon.setVisibility(View.GONE);
            tvAiIcon.setVisibility(View.GONE);

            if (chatMessage.getAuthor() == ChatMessage.AUTHOR_TYPE.AUTHOR_TYPE_USER) {
                tvUsersIcon.setVisibility(View.VISIBLE);
                llMessageContainer.setGravity(Gravity.RIGHT);
            } else {
                tvAiIcon.setVisibility(View.VISIBLE);
                llMessageContainer.setGravity(Gravity.LEFT);
            }

            tvMessageText.setText(chatMessage.getMessage());
        }

    }

}