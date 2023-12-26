package com.example.app_iliyan.view.components.message

import android.widget.PopupMenu.OnDismissListener
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.view_model.MessageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MessageBubble(message: Message) {
  val showMessageMenu = remember { mutableStateOf(false) }
  val clickedMessage = remember { mutableStateOf(message) }

  val currentEmail = LocalData.getAuthenticatedUser()?.email ?: ""
  val isUsernameMe = if (currentEmail == message.sender.email) "me" else message.sender.username
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isUsernameMe == "me") Arrangement.End else Arrangement.Start
  ) {
    Column {
      Surface(
        onClick = { showMessageMenu.value = true },
        shape =
          if (isUsernameMe == "me")
            RoundedCornerShape(
              topStart = 8.dp,
              topEnd = 8.dp,
              bottomEnd = 0.dp,
              bottomStart = 16.dp
            )
          else
            RoundedCornerShape(
              topStart = 8.dp,
              topEnd = 8.dp,
              bottomEnd = 16.dp,
              bottomStart = 0.dp
            ),
        tonalElevation = 1.dp,
        color =
          if (isUsernameMe == "me") Color(red = 234, green = 221, blue = 255) else Color.LightGray,
        modifier = Modifier.padding(8.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(text = isUsernameMe, fontWeight = FontWeight.Bold, fontSize = 16.sp)
          Text(text = message.content, fontSize = 15.sp)

          Spacer(modifier = Modifier.height(4.dp))
          AttachmentImage(encodedAttachment = message.attachmentURL)

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = message.timestamp,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic,
            fontSize = 12.sp
          )
        }
        // Show message menu
        if (showMessageMenu.value && isUsernameMe == "me") {
          MessageDropDownMenu(
            OnDismissListener { showMessageMenu.value = false },
            clickedMessage.value
          )
        }
      }
    }
  }
}

@Composable
fun MessageDropDownMenu(OnDismissListener: OnDismissListener, clickedMessage: Message) {

  val messageViewModel: MessageViewModel = viewModel()

  Box(contentAlignment = Alignment.Center) {
    MaterialTheme(
      colorScheme =
        MaterialTheme.colorScheme.copy(surface = Color(red = 234, green = 221, blue = 255))
    ) {
      DropdownMenu(
        expanded = true,
        onDismissRequest = { OnDismissListener.onDismiss(null) },
        modifier = Modifier.align(Alignment.TopEnd)
      ) {
        DropdownMenuItem(
          onClick = {
            CoroutineScope(Dispatchers.Main).launch {
              messageViewModel.handleDeleteMessageClick(clickedMessage)
            }
          },
          text = { Text("Delete") },
          leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = null) }
        )
      }
    }
  }
}
