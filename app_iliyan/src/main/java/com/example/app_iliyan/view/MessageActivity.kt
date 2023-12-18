package com.example.app_iliyan.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.model.User
import com.example.app_iliyan.navigation.MessageNavigationHandler
import com.example.app_iliyan.view.components.message.MessageBubble
import com.example.app_iliyan.view_model.MessageViewModel
import kotlinx.serialization.json.Json

class MessageActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val messageNavigationHandler = MessageNavigationHandler()
    val messageViewModel: MessageViewModel by viewModels()

    val groupChatJSON = intent?.getStringExtra("groupChat") ?: ""
    val groupChat: GroupChatData? =
      try {
        Json.decodeFromString(GroupChatData.serializer(), groupChatJSON)
      } catch (e: Exception) {
        null
      }

    setContent {
      val groupChatState = messageViewModel.selectedGroupChat.collectAsState()
      if (groupChat != null) {
        messageViewModel.fetchMessagesByGroupChat(groupChat)
      }

      MessageLayout(groupChat = groupChatState.value)
    }

    // Back to home event
    messageNavigationHandler.observeBackToHomeEvent(
      lifecycleOwner = this,
      messageViewModel = messageViewModel,
      context = this
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageLayout(groupChat: GroupChat) {

  val searchedFieldUsername = remember { mutableStateOf("") }
  val typedMessage = remember { mutableStateOf("") }
  val filteredMessages: List<Message> =
    groupChat.messages.filter {
      it.sender.username.contains(searchedFieldUsername.value, ignoreCase = true)
    }

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
    Text(
      text = "Chat: " + groupChat.name,
      color = Color.Black,
      style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
    )
    Column(modifier = Modifier.fillMaxWidth().padding(25.dp)) {
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          CircularProgressIndicator()
        }
      } else {
        LazyColumn { items(filteredMessages) { message -> MessageBubble(message = message) } }
      }
    }
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
      onClick = { /* Handle send message action */},
      modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
      colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
      Icon(imageVector = Icons.Default.Send, contentDescription = "", tint = Color.Black)
    }
  }
}

@Composable
@Preview(showBackground = true)
fun GreetingPreview() {
  val groupChat =
    GroupChat(
      1,
      "GroupChat",
      listOf(User("username", "email", "pass")),
      listOf(
        Message(
          1,
          "Hello",
          "https://www.google.com",
          "10:10:10",
          User("username", "email", "pass")
        ),
        Message(1, "Hello", "https://www.google.com", "10:10:10", User("username", "email", "pass"))
      )
    )
  MessageLayout(groupChat = groupChat)
}
