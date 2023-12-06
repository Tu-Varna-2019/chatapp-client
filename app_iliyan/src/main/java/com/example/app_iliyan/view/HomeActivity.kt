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
import com.example.app_iliyan.view.components.ChatNavigationBottomMenu
import com.example.app_iliyan.view.components.SnackbarManager.ScaffoldSnackbar
import com.example.app_iliyan.view_model.HomeViewModel

class HomeActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val userOptionsObj = UserOptions("", "Chat")
    val viewModel: HomeViewModel by viewModels()

    setContent {
      val groupchatListState = viewModel.groupChats.collectAsState()
      viewModel.loadGroupChats()

      ScaffoldSnackbar {
        HomeLayout(groupchatList = groupchatListState.value, userOptions = userOptionsObj)
      }
    }
  }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeLayout(groupchatList: List<GroupChat>, userOptions: UserOptions = UserOptions("", "Chat")) {
  ChatNavigationBottomMenu(items = groupchatList, userOptions = userOptions)
}

@Preview(showBackground = true)
@Composable
fun HomeLayoutPreview() {
  val groupchatList: List<GroupChat> =
      listOf(
          GroupChat("Group 1", listOf()),
          GroupChat("Group 2", listOf()),
          GroupChat("Group 3", listOf()),
          GroupChat("Group 4", listOf()),
          GroupChat("Group 5", listOf()),
          GroupChat("Group 6", listOf()))

  HomeLayout(groupchatList = groupchatList)
}
