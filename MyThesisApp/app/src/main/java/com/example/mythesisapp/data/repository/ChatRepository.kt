package com.example.mythesisapp.data.repository

import com.example.mythesisapp.data.model.Message
import com.example.mythesisapp.data.model.MessageRequest
import com.example.mythesisapp.data.model.OpenAIApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ChatRepository @Inject constructor(private val apiService: OpenAIApiService) {

    fun sendMessage(message: String): Flow<Result<String>> = flow {
        val requestMap = MessageRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(Message(content = message))
        )

        try {
            val response = apiService.sendMessageToChatGPT(requestMap)
            if (response.isSuccessful) {
                emit(Result.success(response.body()?.choices?.first()?.message?.content.plus("\n")))
            } else {
                emit(Result.failure(Exception("Failed to send message.")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}