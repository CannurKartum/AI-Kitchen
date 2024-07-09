package com.example.mythesisapp.data.model

data class MessageResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ContentResponse
)

data class ContentResponse(
    val content: String
)