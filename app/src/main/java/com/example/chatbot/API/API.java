package com.example.chatbot.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.chatbot.API.models.ResponsePost;

public interface API {
    // Initiate and save new conversation server-sided
    @FormUrlEncoded
    @POST("/verify-username")
    Call<ResponsePost> initUser(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("/chat")
    Call<ResponsePost> getChatResponse(
            @Field("username") String username,
            @Field("message") String message
    );
}
