package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.LocalData

class GroupChatRepo : SharedRepo() {
  suspend fun getAllGroupChatsAuthUser(): List<GroupChat> {
    try {
      val server: ServerResponse =
        encodeAndSendUserDataByEvent("GetGroupChatsAuthUser", LocalData.getAuthenticatedUser()!!)

      if (server.response.status == "Success" && server.response.groupchats != null) {

        val groupchatList =
          server.response.groupchats.map { groupChatData ->
            ServerDataHandler.convertGroupChatDataToModel(groupChatData.groupchat)
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
}
