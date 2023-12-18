package com.example.app_iliyan.view.components.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.Message

@Composable
fun MessageBubble(message: Message) {
  val currentEmail = LocalData.getAuthenticatedUser()?.email ?: ""
  val isUsernameMe = if (currentEmail == message.sender.email) "me" else message.sender.username
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isUsernameMe == "me") Arrangement.End else Arrangement.Start
  ) {
    Column {
      Surface(
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
          Text(
            text = message.timestamp,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic,
            fontSize = 12.sp
          )
        }
      }
    }
  }
}
