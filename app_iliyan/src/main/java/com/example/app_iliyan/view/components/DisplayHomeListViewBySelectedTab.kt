package com.example.app_iliyan.view.components

import androidx.compose.runtime.Composable
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.User
import com.example.app_iliyan.model.state.UserOptions

@Composable
fun DisplayHomeListViewBySelectedTab(
    userOptions: UserOptions,
    filteredGroupChat: List<GroupChat>,
    filteredContactChat: List<FriendRequest>,
    currentUser: User
) {
  when (userOptions.selectedTab) {
    "Chat" -> {
      GroupChatListView(items = filteredGroupChat)
    }
    "Contacts" -> {
      ContactsListView(items = filteredContactChat)
    }
    "Settings" -> {
      SettingsOptionListView()
    }
  }
}
