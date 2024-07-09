package com.example.mythesisapp.data.model

import dagger.Provides
import retrofit2.http.POST
import retrofit2.http.Headers
import retrofit2.http.Body
import retrofit2.Response
import retrofit2.http.GET


interface OpenAIApiService {

    @POST("v1/chat/completions")
    suspend fun sendMessageToChatGPT(@Body message: MessageRequest): Response<MessageResponse>

    @GET("messages/recent")
    suspend fun fetchRecentMessages(): Response<List<Message>>

    @POST("messages/send")
    suspend fun sendMessage(@Body message: Message): Response<Message>

}