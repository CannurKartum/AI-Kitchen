package com.example.mythesisapp.ui.aichat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythesisapp.data.model.Message
import com.example.mythesisapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    init {
        viewModelScope.launch {
            _messages.value += ChatMessage.Model("Hello! How can I help you today?")
        }
    }

    fun sendMessageToChatGPT(userMessage: String) {
        viewModelScope.launch {
            _messages.value += ChatMessage.User(userMessage)
            _messages.value += ChatMessage.Loading
            chatRepository.sendMessage(userMessage).collect { result ->
                result.onSuccess {
                    _messages.value -= ChatMessage.Loading
                    _messages.value += ChatMessage.Model(it)
                }.onFailure {
                    // Handle error
                    _messages.value -= ChatMessage.Loading
                }
            }
        }
    }
}

sealed interface ChatMessage {
    data object Loading : ChatMessage
    data class User(
        val text: String,
    ) : ChatMessage

    data class Model(
        val text: String,
    ) : ChatMessage
}