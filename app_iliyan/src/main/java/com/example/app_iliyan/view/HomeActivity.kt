package com.example.app_iliyan.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.state.UserOptions
import com.example.app_iliyan.view.components.ChatNavigationBottomMenu
import com.example.app_iliyan.view.components.SnackbarManager.ScaffoldSnackbar
import com.example.app_iliyan.view_model.HomeViewModel

class HomeActivity : ComponentActivity() {

  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val userOptionsObj = UserOptions("", "Chat")
    val groupchatList: List<GroupChat> = HomeViewModel.getAllGroupChatsAuthUser()
    //        listOf(
    //            GroupChat(
    //                "Group 1",
    //                listOf(
    //                    User("Username1", "email", "password"), User("User 2", "email",
    // "password"))),
    //            GroupChat("Group 2", listOf()),
    //            GroupChat("Group 3", listOf()),
    //            GroupChat("Group 4", listOf()),
    //            GroupChat("Group 5", listOf()),
    //            GroupChat("Group 6", listOf()))

    setContent {
      ScaffoldSnackbar {
        val context = LocalContext.current
        HomeLayout(groupchatList = groupchatList, userOptions = userOptionsObj)
      }
    }
  }
}

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
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
