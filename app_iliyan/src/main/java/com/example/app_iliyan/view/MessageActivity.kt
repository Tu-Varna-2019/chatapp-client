package com.example.app_iliyan.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.model.User
import com.example.app_iliyan.navigation.MessageNavigationHandler
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
      val messageListState = messageViewModel.messages.collectAsState()

      if (groupChat != null) {
        messageViewModel.fetchMessages(groupChat.id)
      }

      MessageLayout(messageList = messageListState.value)
    }

    // Back to home event
    messageNavigationHandler.observeBackToHomeEvent(
      lifecycleOwner = this,
      messageViewModel = messageViewModel,
      context = this
    )
  }
}

@Composable
fun MessageLayout(messageList: List<Message>) {
  for (message in messageList) {
    Text(text = message.content)
  }
}

@Composable
@Preview(showBackground = true)
fun GreetingPreview() {
  val messageList =
    Message(1, "Hello", "https://www.google.com", "10:10:10", User("username", "email", "pass"))

  MessageLayout(messageList = listOf(messageList))
}
