package com.example.app_iliyan.view.components.message

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.view.components.LoadingAnimation
import com.example.app_iliyan.view_model.MessageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFieldMessages(groupChat: GroupChat) {
  Column(modifier = Modifier.fillMaxWidth().padding(25.dp)) {
    val searchedFieldUsername = remember { mutableStateOf("") }
    val filteredMessages: List<Message> =
      groupChat.messages.filter {
        it.sender.username.contains(searchedFieldUsername.value, ignoreCase = true)
      }

    TextField(
      value = searchedFieldUsername.value,
      onValueChange = { newText -> searchedFieldUsername.value = newText },
      modifier = Modifier.fillMaxWidth().padding(16.dp).fillMaxWidth(0.8f),
      shape = RoundedCornerShape(22.dp),
      leadingIcon = {
        Icon(imageVector = Icons.Default.Search, contentDescription = "Search here")
      },
      label = { Text("Search for email...") },
      colors =
        TextFieldDefaults.textFieldColors(
          containerColor = Color(red = 234, green = 221, blue = 255),
          cursorColor = Color.Black,
          focusedIndicatorColor = Color.Transparent,
          unfocusedIndicatorColor = Color.Transparent
        ),
      singleLine = true
    )

    if (filteredMessages.isEmpty()) {
      LoadingAnimation()
    } else {
      LazyColumn { items(filteredMessages) { message -> MessageBubble(message = message) } }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessage(groupChat: GroupChat) {
  val typedMessage = remember { mutableStateOf("") }
  val messageViewModel: MessageViewModel = viewModel()
  val isLoading = remember { mutableStateOf(false) }

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
    Text(
      text = groupChat.name,
      color = Color.Black,
      style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
    )

    if (isLoading.value) LoadingAnimation()

    TextField(
      value = typedMessage.value,
      onValueChange = { newText -> typedMessage.value = newText },
      modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(16.dp),
      shape = RoundedCornerShape(22.dp),
      label = { Text("Type a message...") },
      colors =
        TextFieldDefaults.textFieldColors(
          containerColor = Color(red = 234, green = 221, blue = 255),
          cursorColor = Color.Black,
          focusedIndicatorColor = Color.Transparent,
          unfocusedIndicatorColor = Color.Transparent
        ),
      singleLine = true
    )
    Button(
      onClick = {
        CoroutineScope(Dispatchers.Main).launch {
          isLoading.value = true
          messageViewModel.handleSendMessageClick(groupChat, typedMessage.value)
          typedMessage.value = ""
          isLoading.value = false
        }
      },
      modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
      colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
      Icon(imageVector = Icons.Default.Send, contentDescription = "", tint = Color.Black)
    }
  }
}
