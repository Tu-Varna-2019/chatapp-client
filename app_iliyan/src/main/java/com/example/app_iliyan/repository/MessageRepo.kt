package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.GroupChat

class MessageRepo : SharedRepo() {
  suspend fun getAllMessages(groupChatDataArg: GroupChatData): GroupChat {
    // Convert the GroupChatData to a GroupChat model
    val groupChatArg = ServerDataHandler.convertGroupChatDataToModel(groupChatDataArg)
    try {
      val server: ServerResponse =
        encodeAndSendIDByEvent("GetMessagesByGroupID", groupChatDataArg.id)

      if (server.response.status == "Success" && server.response.messages != null) {

        val messageList =
          server.response.messages.map { messageData ->
            ServerDataHandler.convertMessageDataToModel(messageData.message)
          }

        groupChatArg.messages = messageList

        return groupChatArg
      } else {
        Utils.logger.warn("getAllMessages: Not Messages Found")
        return groupChatArg
      }
    } catch (e: Exception) {
      Utils.logger.error("getAllMessages: {}", e.message.toString())
    }
    return groupChatArg
  }
}
