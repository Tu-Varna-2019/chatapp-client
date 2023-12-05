package com.example.app_iliyan.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_iliyan.R
import com.example.app_iliyan.model.GroupChat

@Composable
fun GroupChatListView(items: List<GroupChat>) {
  LazyColumn { items(items) { item -> GroupChatItem(item) } }
}

@Composable
fun GroupChatItem(item: GroupChat) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
  Card(
      modifier =
          Modifier.padding(0.dp)
              //.width(250.dp)
              //.clickable {  }
              .fillMaxSize()
              .padding(8.dp),
      elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White))
  {
        Row(modifier = Modifier.padding(18.dp)) {
          Image(
              painter = painterResource(id = R.drawable.group),
              contentDescription = item.name,
              modifier = Modifier.size(40.dp, 40.dp))

          Spacer(modifier = Modifier.width(16.dp))
          Column {
            Text(text = item.name, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))

            item.users.forEach { user ->
              Text(user.username, style = TextStyle(fontSize = 10.sp))
            }
          }
        }
      }}
}