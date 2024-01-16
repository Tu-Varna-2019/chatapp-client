package com.example.app_iliyan.view.components.message

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_iliyan.R
import com.example.app_iliyan.dataclass.FilterRequest
import com.example.app_iliyan.dataclass.FriendRequestData
import com.example.app_iliyan.dataclass.UserData
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.view.components.isChatLoadedIndicator
import com.example.app_iliyan.view_model.FriendRequestViewModel
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
      modifier = Modifier.fillMaxWidth().padding(36.dp).fillMaxWidth(0.8f),
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
      isChatLoadedIndicator(
        messageContent = "No messages found!",
        isChatLoaded = filteredMessages.isEmpty(),
        image = R.drawable.group
      )
    } else {
      LazyColumn { items(filteredMessages) { message -> MessageBubble(message = message) } }
    }
  }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessage(groupChat: GroupChat) {
  val showUsersList = remember { mutableStateOf(false) }
  val typedMessage = remember { mutableStateOf("") }
  val messageViewModel: MessageViewModel = viewModel()
  val isLoading = remember { mutableStateOf(false) }

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
    Text(
      text = groupChat.name,
      color = Color.Black,
      style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.SemiBold),
      modifier = Modifier.align(Alignment.TopCenter).padding(16.dp)
    )

    Button(
      onClick = { messageViewModel.backHomeEvent.value = true },
      modifier = Modifier.align(Alignment.TopStart).padding(16.dp),
      colors =
        ButtonDefaults.buttonColors(contentColor = Color.Black, containerColor = Color.Transparent)
    ) {
      Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
    }

    Button(
      onClick = { showUsersList.value = !showUsersList.value },
      modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
      colors =
        ButtonDefaults.buttonColors(contentColor = Color.Black, containerColor = Color.Transparent)
    ) {
      Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
    }

    if (isLoading.value)
      isChatLoadedIndicator(
        messageContent = "No messages found!",
        isChatLoaded = isLoading.value,
        image = R.drawable.group
      )

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

    Row(
      modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
      horizontalArrangement = Arrangement.End
    ) {
      Button(
        onClick = { messageViewModel.handleAddAttachmentClick() },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "", tint = Color.Black)
      }

      Spacer(modifier = Modifier.width(8.dp))

      Button(
        enabled = !typedMessage.value.isEmpty(),
        onClick = {
          CoroutineScope(Dispatchers.Main).launch {
            isLoading.value = true
            messageViewModel.handleSendMessageClick(groupChat, typedMessage.value)
            typedMessage.value = ""
            isLoading.value = false
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
      ) {
        Icon(imageVector = Icons.Default.Send, contentDescription = "", tint = Color.Black)
      }
    }
    if (showUsersList.value) {
      UsersToolbar()
    }
  }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UsersToolbar() {
  val showAcceptedFriendRequests = remember { mutableStateOf(false) }

  val friendRequestViewModel: FriendRequestViewModel = viewModel()

  val groupchatViewModel: MessageViewModel = viewModel()
  val groupChatListState = groupchatViewModel.selectedGroupChat.collectAsState()

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
    Surface(
      modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(16.dp),
      shape =
        RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomEnd = 0.dp, bottomStart = 16.dp),
      tonalElevation = 1.dp,
      color = Color(red = 234, green = 221, blue = 255)
    ) {
      Column(
        modifier = Modifier.fillMaxWidth().align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = if (!showAcceptedFriendRequests.value) "Group members" else "Your friends",
          color = Color.Black,
          textAlign = TextAlign.Center,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        if (!showAcceptedFriendRequests.value) {
          GroupChatUsersCardList(
            items = groupChatListState.value.users,
            groupChatId = groupChatListState.value.id
          )
        } else {
          val friendRequestResult =
            FilterRequest(
              friendrequest =
                FriendRequestData(
                  id = 0,
                  status = "Accepted",
                  recipient = UserData(id = 0, username = "", email = "", password = ""),
                  sender = UserData(id = 0, username = "", email = "", password = "")
                )
            )

          friendRequestViewModel.fetchFriendRequests(friendRequestResult)
          val friendRequestsListState = friendRequestViewModel.friendRequests.collectAsState()
          FriendRequestCardList(
            friendRequestsListState.value,
            "AddUserToGroup",
            groupChatListState.value.id
          )
        }
        // Add user button
        Button(
          onClick = { showAcceptedFriendRequests.value = !showAcceptedFriendRequests.value },
          colors =
            ButtonDefaults.buttonColors(
              contentColor = Color.Black,
              containerColor = Color.Transparent
            )
        ) {
          Icon(
            imageVector =
              if (!showAcceptedFriendRequests.value) Icons.Default.AddCircle
              else Icons.Default.ArrowBack,
            contentDescription = ""
          )
          Text(
            text = if (!showAcceptedFriendRequests.value) "Add User" else "Go back",
            color = Color.Black
          )
        }
      }
    }
  }
}
