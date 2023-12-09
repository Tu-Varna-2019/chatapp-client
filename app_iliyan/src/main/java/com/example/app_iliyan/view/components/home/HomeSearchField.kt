package com.example.app_iliyan.view.components.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.state.UserOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchField(
  groupchatList: List<GroupChat>,
  friendrequestList: List<FriendRequest>,
  userOptions: UserOptions
) {
  // Filter the group chat list / contacts  by the search text
  val filteredGroupChat =
    groupchatList.filter { it.name.contains(userOptions.searchText, ignoreCase = true) }
  val filteredContactChat: List<FriendRequest> =
    friendrequestList.filter { it.status.contains(userOptions.searchText, ignoreCase = true) }

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
    Text(
      text = userOptions.selectedTab,
      color = Color.Black,
      style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
    )
    Column(modifier = Modifier.fillMaxWidth().padding(25.dp)) {

      // Hide the search field if the selected tab is "Settings"
      if (userOptions.selectedTab != "Settings") {

        TextField(
          value = userOptions.searchText,
          onValueChange = { newText -> userOptions.searchText = newText },
          modifier = Modifier.fillMaxWidth().padding(16.dp).fillMaxWidth(0.8f),
          shape = RoundedCornerShape(22.dp),
          leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search here")
          },
          label = { Text("Search something...") },
          colors =
            TextFieldDefaults.textFieldColors(
              containerColor = Color(red = 234, green = 221, blue = 255),
              cursorColor = Color.Black,
              focusedIndicatorColor = Color.Transparent,
              unfocusedIndicatorColor = Color.Transparent
            ),
          singleLine = true
        )
      }
      Spacer(modifier = Modifier.height(16.dp))

      // Display filtered list by selected tab
      when (userOptions.selectedTab) {
        "Chat" -> {
          GroupChatList(items = filteredGroupChat)
        }
        "Contacts" -> {
          ContactList(items = filteredContactChat)
        }
        "Settings" -> {
          SettingsOptionList()
        }
      }
    }
  }
}
