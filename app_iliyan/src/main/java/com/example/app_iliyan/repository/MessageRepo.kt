package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.Message

class MessageRepo : SharedRepo() {
  suspend fun getAllMessages(groupChatDataArg: GroupChatData): GroupChat {
    // Convert the GroupChatData to a GroupChat model
    val groupChatArg = ServerDataHandler.convertGroupChatDataToModel(groupChatDataArg)
    try {
      val server: ServerResponse = sendIDData("GetMessages", groupChatDataArg.id)

      if (server.status == "Success" && server.data.messages != null) {

        val messageList =
          server.data.messages.map { messageData ->
            ServerDataHandler.convertMessageDataToModel(messageData)
          }
        groupChatArg.messages = messageList
      } else {
        Utils.logger.warn("getAllMessages: Not Messages Found")
        return groupChatArg
      }
    } catch (e: Exception) {
      Utils.logger.error("getAllMessages: {}", e.message.toString())
    }
    return groupChatArg
  }

  suspend fun sendMessage(
    groupChatID: String,
    typedMessage: String,
    attachmentURL: String
  ): List<Message> {
    try {
      val server: ServerResponse =
        sendMessageData("SendMessage", groupChatID, typedMessage, attachmentURL)

      if (server.status == "Success" && server.data.messages != null) {

        val messageList =
          server.data.messages.map { messageData ->
            ServerDataHandler.convertMessageDataToModel(messageData)
          }

        return messageList
      } else {
        Utils.logger.warn("sendMessage: Not Messages updated")
        return emptyList()
      }
    } catch (e: Exception) {
      Utils.logger.error("sendMessage: {}", e.message.toString())
    }
    return emptyList()
  }

  suspend fun deleteMessage(messageID: Int): Boolean {
    try {
      val server: ServerResponse = sendIDData("DeleteMessage", messageID)

      if (server.status == "Success") {
        return true
      } else {
        Utils.logger.warn("deleteMessage: Not Messages deleted")
        return false
      }
    } catch (e: Exception) {
      Utils.logger.error("deleteMessage: {}", e.message.toString())
    }
    return false
  }
}
