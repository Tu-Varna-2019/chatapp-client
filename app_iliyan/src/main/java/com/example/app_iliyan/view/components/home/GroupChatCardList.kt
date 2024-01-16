package com.example.app_iliyan.view.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_iliyan.R
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.view.components.dialog_box.AlertDialogManager
import com.example.app_iliyan.view.components.dialog_box.SnackbarManager
import com.example.app_iliyan.view.components.isChatLoadedIndicator
import com.example.app_iliyan.view_model.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GroupChatCardList(items: List<GroupChat>) {
  if (items.isEmpty()) {
    isChatLoadedIndicator(
      messageContent = "No group chats found!",
      isChatLoaded = items.isEmpty(),
      image = R.drawable.group
    )
  } else {
    LazyColumn { items(items) { item -> GroupChatCard(item) } }
  }
}

@Composable
fun GroupChatCard(item: GroupChat) {

  val showDeleteGroupChatDialog = remember { mutableStateOf(false) }

  val homeViewModel: HomeViewModel = viewModel()

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Card(
      modifier =
        Modifier.padding(0.dp)
          .clickable { homeViewModel.handleGotoMessageClick(item) }
          .fillMaxSize()
          .padding(8.dp),
      elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Row(
        modifier = Modifier.padding(18.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Image(
            painter = painterResource(id = R.drawable.group),
            contentDescription = item.name,
            modifier = Modifier.size(40.dp, 40.dp)
          )

          Spacer(modifier = Modifier.width(16.dp))

          Column {
            Text(
              text = item.name,
              style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            item.users.forEach { user -> Text(user.username, style = TextStyle(fontSize = 10.sp)) }
          }
        }

        IconButton(onClick = { showDeleteGroupChatDialog.value = true }) {
          Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = Color(red = 150, green = 100, blue = 200)
          )

          if (showDeleteGroupChatDialog.value) {
            AlertDialogManager(
              title = "Delete Group chat",
              message = "Are you sure you want to delete your group chat: ${item.name} ?",
              onConfirm = {
                CoroutineScope(Dispatchers.Main).launch {
                  showDeleteGroupChatDialog.value = false
                  val message = homeViewModel.handleDeleteGroupChatClick(item.id)
                  SnackbarManager.showSnackbar(message)
                }
              },
              onDismiss = { showDeleteGroupChatDialog.value = false }
            )
          }
        }
      }
    }
  }
}
