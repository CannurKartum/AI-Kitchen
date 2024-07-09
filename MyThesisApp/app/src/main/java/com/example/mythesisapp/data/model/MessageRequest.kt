package com.example.mythesisapp.data.model

data class MessageRequest(
    val messages: List<Message>,
    val model: String = "gpt-4"
)

data class Message(
    val role: String = "user",
    val content: String
)