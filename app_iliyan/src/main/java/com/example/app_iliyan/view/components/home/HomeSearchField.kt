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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_iliyan.dataclass.FilterRequest
import com.example.app_iliyan.dataclass.FriendRequestData
import com.example.app_iliyan.dataclass.UserData
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.state.UserOptions
import com.example.app_iliyan.view.components.message.FriendRequestCardList
import com.example.app_iliyan.view_model.FriendRequestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchField(groupchatList: List<GroupChat>, userOptions: UserOptions) {

  val selectedFQTab = remember { mutableStateOf("Sent") }
  val friendRequestViewModel: FriendRequestViewModel = viewModel()
  var filteredFriendRequest: List<FriendRequest>
  var filterRequest: FilterRequest? = null
  var fqDropDownMenuMode: String = ""

  // Filter the group chat / friends  by the search text
  val filteredGroupChat =
    groupchatList.filter { it.name.contains(userOptions.searchText, ignoreCase = true) }

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
          GroupChatCardList(items = filteredGroupChat)
        }
        "Friends" -> {
          FriendRequestTopTabBar(
            selectedTab = selectedFQTab.value,
            onTabSelected = { selectedFQTab.value = it }
          )

          when (selectedFQTab.value) {
            "Sent" -> {
              filterRequest =
                FilterRequest(
                  friendrequest =
                    FriendRequestData(
                      id = 0,
                      status = "Pending",
                      recipient = UserData(id = 0, username = "", email = "", password = ""),
                      sender = UserData(id = 0, username = "", email = "Non empty", password = "")
                    )
                )
              fqDropDownMenuMode = "CancelPendingFriendRequest"
            }
            "Awaiting" -> {
              filterRequest =
                FilterRequest(
                  friendrequest =
                    FriendRequestData(
                      id = 0,
                      status = "Pending",
                      recipient =
                        UserData(id = 0, username = "", email = "Non empty", password = ""),
                      sender = UserData(id = 0, username = "", email = "", password = "")
                    )
                )
              fqDropDownMenuMode = "AcceptRejectFriendInvitation"
            }
            "Added friends" -> {
              filterRequest =
                FilterRequest(
                  friendrequest =
                    FriendRequestData(
                      id = 0,
                      status = "Accepted",
                      recipient = UserData(id = 0, username = "", email = "", password = ""),
                      sender = UserData(id = 0, username = "", email = "", password = "")
                    )
                )
              fqDropDownMenuMode = "RemoveAcceptedFriend"
            }
          }

          LaunchedEffect(selectedFQTab.value) {
            friendRequestViewModel.fetchFriendRequests(filterRequest)
          }

          val friendRequestsListState = friendRequestViewModel.friendRequests.collectAsState()

          val filteredFriendRequestDerived by remember {
            derivedStateOf {
              friendRequestsListState.value.filter {
                it.status.contains(userOptions.searchText, ignoreCase = true)
              }
            }
          }

          FriendRequestCardList(items = filteredFriendRequestDerived, fqDropDownMenuMode)
        }
        "Settings" -> {
          SettingsOptions()
        }
      }
    }
  }
}

@Composable
fun FriendRequestTopTabBar(selectedTab: String, onTabSelected: (String) -> Unit) {
  val tabOptions = listOf("Sent", "Awaiting", "Added friends")

  TabRow(selectedTabIndex = tabOptions.indexOf(selectedTab)) {
    tabOptions.forEachIndexed { index, text ->
      Tab(selected = selectedTab == text, onClick = { onTabSelected(text) }, text = { Text(text) })
    }
  }
}
