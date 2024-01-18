package com.example.app_iliyan.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.state.UserOptions
import com.example.app_iliyan.navigation.HomeNavigationHandler
import com.example.app_iliyan.view.components.dialog_box.SnackbarManager
import com.example.app_iliyan.view.components.home.HomeNavigationBottomMenu
import com.example.app_iliyan.view_model.HomeViewModel

class HomeActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // TODO: Remove this
    // LocalData.setAuthenticatedUser("me2", "me@me.bg")

    val homeNavigationHandler = HomeNavigationHandler()
    val userOptionsObj = UserOptions("", "Chat")
    val homeViewModel: HomeViewModel by viewModels()

    setContent {
      val groupchatListState = homeViewModel.groupChats.collectAsState()
      homeViewModel.fetchGroupChats()

      SnackbarManager.ScaffoldSnackbar {
        HomeLayout(groupchatList = groupchatListState.value, userOptions = userOptionsObj)
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
fun HomeLayout(groupchatList: List<GroupChat>, userOptions: UserOptions = UserOptions("", "Chat")) {
  HomeNavigationBottomMenu(groupchatList = groupchatList, userOptions = userOptions)
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

  HomeLayout(groupchatList = groupchatList)
}
