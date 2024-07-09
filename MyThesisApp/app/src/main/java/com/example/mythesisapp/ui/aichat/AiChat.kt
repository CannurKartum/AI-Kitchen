package com.example.mythesisapp.ui.aichat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mythesisapp.ui.navigation.HomeRoutes
import kotlinx.coroutines.delay

@Composable
fun AiChat(
    chatViewModel: ChatViewModel = hiltViewModel(),
    navController: NavController
) {
    Column(Modifier.fillMaxSize()) {
        val messages = chatViewModel.messages.collectAsState().value

        IconButton(onClick = { navController.navigate(HomeRoutes.Main.name) }) {
            Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Go back")
        }
        Column(modifier = Modifier.fillMaxSize()) {
            MessageList(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                messages = messages
            )
            UserInput(onMessageSent = { messageText ->
                chatViewModel.sendMessageToChatGPT(messageText)
            })
        }
    }
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<ChatMessage>
) {
    LazyColumn(modifier = modifier) {
        items(messages) { message ->
            ChatItem(message = message)
        }
    }
}

@Composable
fun ChatItem(message: ChatMessage) {

    val isUser = message is ChatMessage.User

    val backgroundColor = if (isUser) Color.Red else Color.Blue

    val shape = RoundedCornerShape(
        topStart = if (isUser) 20.dp else 4.dp,
        topEnd = if (isUser) 4.dp else 20.dp,
        bottomStart = 20.dp,
        bottomEnd = 20.dp,
    )

    val horizontalAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Card(
            modifier = Modifier.align(horizontalAlignment),
            colors = CardDefaults.cardColors(backgroundColor),
            shape = shape,
        ) {
            when (message) {
                ChatMessage.Loading -> {
                    var dotsIndex by remember { mutableIntStateOf(0) }
                    val dots = listOf(".", "..", "...")
                    LaunchedEffect(Unit) {
                        while (true) {
                            delay(500)
                            dotsIndex = (dotsIndex + 1) % dots.size
                        }
                    }
                    ChatBubbleText("Loading".plus(dots[dotsIndex]))
                }

                is ChatMessage.User -> ChatBubbleText(message.text)
                is ChatMessage.Model -> ChatBubbleText(message.text)
            }
        }
    }
}

@Composable
fun ChatBubbleText(
    message: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = message,
            color = Color.White,
        )
    }
}

@Composable
fun UserInput(onMessageSent: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(8.dp)) {
        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Type a message...") }
        )
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onMessageSent(text)
                    text = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Send")
        }
    }
}

