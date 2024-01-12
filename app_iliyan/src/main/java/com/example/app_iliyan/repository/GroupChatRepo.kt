package com.example.app_iliyan.repository

import android.util.Log
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.LocalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroupChatRepo : SharedRepo() {
  suspend fun getAllGroupChatsAuthUser(): List<GroupChat> {
    try {
      val server: ServerResponse = sendUserData("GetGroupChats", LocalData.getAuthenticatedUser()!!)

      if (server.response.status == "Success" && server.response.groupchats != null) {

        val groupchatList =
          server.response.groupchats.map { groupChatData ->
            ServerDataHandler.convertGroupChatDataToModel(groupChatData)
          }

        return groupchatList
      } else {
        Utils.logger.warn("getAllGroupChatsAuthUser: Not Chat Groups Found")
        return emptyList()
      }
    } catch (e: Exception) {
      Utils.logger.error("getAllGroupChatsAuthUser: {}", e.message.toString())
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

        if (server.response.status == "Success") {

          onResult(server.response.message)
          return@withContext true
        } else {
          onResult(server.response.status + " : " + server.response.message)
          return@withContext false
        }
      } catch (e: Exception) {
        Log.e("CreateGroupChat", e.message.toString())
        onResult("Error, please try again later")
        return@withContext false
      }
    }
  }
}
