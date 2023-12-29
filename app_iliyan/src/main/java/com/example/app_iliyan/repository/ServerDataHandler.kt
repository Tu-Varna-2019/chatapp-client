package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.FriendRequestData
import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.dataclass.MessageData
import com.example.app_iliyan.dataclass.ResponseContent
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.dataclass.UserData
import com.example.app_iliyan.helpers.NotificationService
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.model.User
import kotlinx.serialization.json.Json

class ServerDataHandler {
  companion object {
    fun parseResponse(jsonString: String): ServerResponse {
      val json = Json { ignoreUnknownKeys = true }
      try {

        val jsonDecoded = json.decodeFromString(ServerResponse.serializer(), jsonString)

        return jsonDecoded
      } catch (e: Exception) {
        Utils.logger.error("Error: parseResponse: {}", e.message.toString())
      }
      return ServerResponse(response = ResponseContent("Error", "Error"))
    }

    fun sentNotification(jsonString: String) {
      val server: ServerResponse = parseResponse(jsonString)

      if (server.response.message == "Notification sent!")
        NotificationService.showNotification(
          LocalData.getAppContext(),
          // attachmentURL = GroupChatName
          server.response.messages?.get(0)?.attachmentURL ?: "",
          server.response.messages?.get(0)?.content ?: "",
          1
        )
    }

    fun convertGroupChatDataToModel(groupChatData: GroupChatData): GroupChat {
      val users =
        groupChatData.users.map { userData ->
          User(
            id = userData.id.toInt(),
            username = userData.username,
            email = userData.email,
            password = userData.password
          )
        }
      return GroupChat(id = groupChatData.id, name = groupChatData.name, users = users)
    }

    fun convertFriendRequestDataToModel(friendrequestData: FriendRequestData): FriendRequest {

      val recipient =
        User(
          id = friendrequestData.recipient.id.toInt(),
          username = friendrequestData.recipient.username,
          email = friendrequestData.recipient.email,
          password = friendrequestData.recipient.password
        )

      return FriendRequest(
        id = friendrequestData.id,
        status = friendrequestData.status,
        recipient = recipient,
        sender = LocalData.getAuthenticatedUser()!!
      )
    }

    fun convertMessageDataToModel(messageData: MessageData): Message {

      val sender =
        User(
          id = messageData.sender.id.toInt(),
          username = messageData.sender.username,
          email = messageData.sender.email,
          password = messageData.sender.password
        )

      return Message(
        id = messageData.id.toInt(),
        content = messageData.content,
        attachmentURL = messageData.attachmentURL,
        timestamp = messageData.timestamp,
        sender = sender
      )
    }

    fun convertListUserToListUserData(users: List<User>): List<UserData> {
      return users.map { user ->
        UserData(user.id.toString(), user.username, user.email, user.password)
      }
    }
  }
}
