package com.example.app_iliyan.view.components.message

import android.widget.PopupMenu
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.app_iliyan.model.User
import com.example.app_iliyan.view.components.dialog_box.SnackbarManager
import com.example.app_iliyan.view.components.isChatLoadedIndicator
import com.example.app_iliyan.view_model.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GroupChatUsersCardList(items: List<User>, groupChatId: Int) {
  if (items.isEmpty()) {
    isChatLoadedIndicator(
      messageContent = "You don't have any users yet!",
      isChatLoaded = items.isEmpty(),
      image = R.drawable.user
    )
  } else {
    LazyColumn { items(items) { item -> GroupChatUsersCard(item, groupChatId) } }
  }
}

@Composable
fun GroupChatUsersCard(item: User, groupChatId: Int) {
  val imageStatus: Int = R.drawable.user
  val showMessageMenu = remember { mutableStateOf(false) }
  val clickedUser = remember { mutableStateOf(item) }

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Card(
      modifier =
        Modifier.padding(0.dp)
          .width(250.dp)
          .clickable { showMessageMenu.value = true }
          .fillMaxSize()
          .padding(8.dp),
      elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Row(modifier = Modifier.padding(18.dp)) {
        Image(
          painter = painterResource(id = imageStatus),
          contentDescription = item.username,
          modifier = Modifier.size(40.dp, 40.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))
        Column {
          Text(
            text = item.email,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
          )
          Spacer(modifier = Modifier.height(8.dp))
        }
        if (showMessageMenu.value) {
          UsersDropDownMenu(
            PopupMenu.OnDismissListener { showMessageMenu.value = false },
            clickedUser.value,
            groupChatId
          )
        }
      }
    }
  }
}

@Composable
fun UsersDropDownMenu(
  OnDismissListener: PopupMenu.OnDismissListener,
  clickedMessage: User,
  groupChatId: Int
) {

  val userViewModel: UserViewModel = viewModel()

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
              val message: String =
                userViewModel.handleRemoveUserFromGroupChatClick(clickedMessage, groupChatId)
              SnackbarManager.showSnackbar(message)
            }
          },
          text = { Text("Delete") },
          leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = null) }
        )
      }
    }
  }
}
