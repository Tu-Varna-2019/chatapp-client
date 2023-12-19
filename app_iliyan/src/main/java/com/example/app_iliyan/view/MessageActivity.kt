package com.example.app_iliyan.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.model.User
import com.example.app_iliyan.navigation.MessageNavigationHandler
import com.example.app_iliyan.view.components.message.SearchFieldMessages
import com.example.app_iliyan.view.components.message.SendMessage
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

    if (groupChat != null) {
      messageViewModel.fetchMessagesByGroupChat(groupChat)
    }
    setContent {
      val groupChatState = messageViewModel.selectedGroupChat.collectAsState()

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

@Composable
fun MessageLayout(groupChat: GroupChat) {
  SearchFieldMessages(groupChat = groupChat)
  SendMessage(groupChat = groupChat)
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
