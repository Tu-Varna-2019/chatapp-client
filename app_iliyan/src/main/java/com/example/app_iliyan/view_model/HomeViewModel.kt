package com.example.app_iliyan.view_model

import android.util.Log
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.state.UserOptions
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    suspend fun getAllGroupChatsAuthUser(): List<GroupChat> {
      return withContext(Dispatchers.Main) {
        try {
          val server: ServerResponse =
              UserViewModel.encodeAndSendUserDataByEvent(
                  "GetGroupChatsAuthUser", LocalData.getAuthenticatedUser()!!)

          if (server.response.status == "Success" && server.response.groupchats != null) {
            server.response.groupchats
          } else {
            emptyList<GroupChat>()
          }
        } catch (e: Exception) {
          Log.e("getAllGroupChatsAuthUser", e.message.toString())
          emptyList<GroupChat>()
        }
        emptyList<GroupChat>()
      }
    }
  }
}
