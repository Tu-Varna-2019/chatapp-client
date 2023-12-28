package com.example.app_iliyan.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.User
import com.example.app_iliyan.model.state.UserOptions
import com.example.app_iliyan.navigation.HomeNavigationHandler
import com.example.app_iliyan.view.components.dialog_box.SnackbarManager.ScaffoldSnackbar
import com.example.app_iliyan.view.components.home.HomeNavigationBottomMenu
import com.example.app_iliyan.view_model.HomeViewModel

class HomeActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // TODO: Remove this
    LocalData.setAuthenticatedUser("me2", "me@me.bg")

    val homeNavigationHandler = HomeNavigationHandler()
    val userOptionsObj = UserOptions("", "Chat")
    val homeViewModel: HomeViewModel by viewModels()

    setContent {
      val groupchatListState = homeViewModel.groupChats.collectAsState()
      homeViewModel.fetchGroupChatsFriendRequests()

      val friendrequestListState = homeViewModel.friendRequests.collectAsState()
      homeViewModel.fetchGroupChatsFriendRequests()

      ScaffoldSnackbar {
        HomeLayout(
          groupchatList = groupchatListState.value,
          friendrequestList = friendrequestListState.value,
          userOptions = userOptionsObj
        )
      }
    }
    // Settings logout event
    homeNavigationHandler.observeLogoutEvent(
      lifecycleOwner = this,
      homeViewModel = homeViewModel,
      context = this
    )

    // GroupChat click message event
    homeNavigationHandler.observeGotoMessageEvent(
      lifecycleOwner = this,
      homeViewModel = homeViewModel,
      context = this
    )
  }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeLayout(
  groupchatList: List<GroupChat>,
  friendrequestList: List<FriendRequest>,
  userOptions: UserOptions = UserOptions("", "Chat")
) {
  HomeNavigationBottomMenu(
    groupchatList = groupchatList,
    friendrequestList = friendrequestList,
    userOptions = userOptions
  )
}

@Preview(showBackground = true)
@Composable
fun HomeLayoutPreview() {
  val groupchatList: List<GroupChat> =
    listOf(
      GroupChat(0, "Group 1", listOf()),
      GroupChat(0, "Group 2", listOf()),
      GroupChat(0, "Group 3", listOf()),
      GroupChat(0, "Group 4", listOf()),
      GroupChat(0, "Group 5", listOf()),
      GroupChat(0, "Group 6", listOf())
    )

  val friendrequestList: List<FriendRequest> =
    listOf(
      FriendRequest(0, "Friend 1", User("1", "1", "1"), User("1", "1", "1")),
      FriendRequest(0, "Friend 2", User("2", "2", "2"), User("1", "1", "1")),
      FriendRequest(0, "Friend 3", User("3", "3", "3"), User("1", "1", "1")),
      FriendRequest(0, "Friend 4", User("4", "4", "4"), User("1", "1", "1")),
      FriendRequest(0, "Friend 5", User("5", "5", "5"), User("1", "1", "1")),
      FriendRequest(0, "Friend 6", User("6", "6", "6"), User("1", "1", "1"))
    )

  HomeLayout(groupchatList = groupchatList, friendrequestList = friendrequestList)
}
