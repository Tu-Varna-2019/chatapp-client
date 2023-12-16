package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.Message

class MessageRepo : SharedRepo() {
  suspend fun getAllMessages(groupChatID: Int): List<Message> {
    try {
      val server: ServerResponse = encodeAndSendIDByEvent("GetMessagesByGroupID", groupChatID)

      if (server.response.status == "Success" && server.response.messages != null) {

        val messageList =
          server.response.messages.map { messageData ->
            ServerDataHandler.convertMessageDataToModel(messageData)
          }

        return messageList
      } else {
        Utils.logger.warn("getAllMessages: Not Messages Found")
        return emptyList()
      }
    } catch (e: Exception) {
      Utils.logger.error("getAllMessages: {}", e.message.toString())
    }
    return emptyList()
  }
}
