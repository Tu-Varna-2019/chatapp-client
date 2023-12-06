package com.example.app_iliyan.view_model

import android.util.Log
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.User
import com.example.app_iliyan.model.state.UserOptions
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel {
  companion object {
    fun handleSelectedTabClick(selectedTab: UserOptions) {
      when (selectedTab.selectedTab) {
        "Chat" -> {
          selectedTab.selectedTab = "Chat"
        }
        "Contacts" -> {
          selectedTab.selectedTab = "Contacts"
        }
        "Settings" -> {
          selectedTab.selectedTab = "Settings"
        }
      }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getAllGroupChatsAuthUser(): List<GroupChat> {
      var groupchatList: List<GroupChat> = ArrayList<GroupChat>()

      try {
        GlobalScope.launch(Dispatchers.Main) {
          val server: ServerResponse =
              UserViewModel.encodeAndSendUserDataByEvent(
                  "GetGroupChatsAuthUser", LocalData.getAuthenticatedUser()!!)

          if (server.response.status == "Success") {
            groupchatList =
                listOf(
                    GroupChat(
                        "Group 1",
                        listOf(
                            User("Username1", "email", "password"),
                            User("User 2", "email", "password"))),
                    GroupChat("Group 2", listOf()),
                    GroupChat("Group 3", listOf()),
                    GroupChat("Group 4", listOf()),
                    GroupChat("Group 5", listOf()),
                    GroupChat("Group 6", listOf()))
          } else {
            // return null
          }
        }
      } catch (e: Exception) {
        Log.e("getAllGroupChatsAuthUser", e.message.toString())
      }
      return groupchatList
    }
  }
}
