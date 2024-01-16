package com.example.app_iliyan.repository

import android.util.Log
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroupChatRepo : SharedRepo() {
  suspend fun getAllGroupChats(): List<GroupChat> {
    try {
      val server: ServerResponse = sendUserData("GetGroupChats", LocalData.getAuthenticatedUser()!!)

      if (server.status == "Success" && server.data.groupchats != null) {

        val groupchatList =
          server.data.groupchats.map { groupChatData ->
            ServerDataHandler.convertGroupChatDataToModel(groupChatData)
          }

        return groupchatList
      } else {
        Utils.logger.warn("getAllGroupChats: Not Chat Groups Found")
        return emptyList()
      }
    } catch (e: Exception) {
      Utils.logger.error("getAllGroupChats: {}", e.message.toString())
    }
    return emptyList()
  }

  suspend fun handleCreateGroupChat(groupchatName: String, onResult: (String) -> Unit): Boolean {
    return withContext(Dispatchers.Main) {
      try {
        val groupChat =
          GroupChat(
            id = 0,
            name = groupchatName,
            users = listOf(LocalData.getAuthenticatedUser()!!)
          )
        val server: ServerResponse = sendGroupChat("CreateGroupChat", groupChat)

        if (server.status == "Success") {

          onResult(server.message)
          return@withContext true
        } else {
          onResult(server.status + " : " + server.message)
          return@withContext false
        }
      } catch (e: Exception) {
        Log.e("CreateGroupChat", e.message.toString())
        onResult("Error, please try again later")
        return@withContext false
      }
    }
  }

  suspend fun handleDeleteGroupChat(groupchatid: Int): String {
    try {
      val server: ServerResponse =
        sendGroupChat(
          "DeleteGroupChat",
          GroupChat(id = groupchatid, name = "", users = emptyList())
        )

      return server.message
    } catch (e: Exception) {
      Utils.logger.error("removeUserFromGroupChat: {}", e.message.toString())
    }
    return "Error, please try again later"
  }

  suspend fun updateUserFromGroupChat(
    event: String,
    user: User,
    groupChatID: Int,
  ): String {
    try {
      val server: ServerResponse = sendUserData(event, user, groupChatID.toString())

      return server.message
    } catch (e: Exception) {
      Utils.logger.error("removeUserFromGroupChat: {}", e.message.toString())
    }
    return "Error, please try again later"
  }
}
