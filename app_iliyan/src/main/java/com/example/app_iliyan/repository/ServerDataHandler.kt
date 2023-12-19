package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.FriendRequestData
import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.dataclass.MessageData
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.dataclass.UserData
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.model.User
import kotlinx.serialization.json.Json

class ServerDataHandler {
  companion object {
    fun parseResponse(jsonString: String): ServerResponse {
      val json = Json { ignoreUnknownKeys = true }
      return json.decodeFromString(ServerResponse.serializer(), jsonString)
    }

    fun convertGroupChatDataToModel(groupChatData: GroupChatData): GroupChat {
      val users =
        groupChatData.users.map { userData ->
          User(username = userData.username, email = userData.email, password = userData.password)
        }
      return GroupChat(id = groupChatData.id, name = groupChatData.name, users = users)
    }

    fun convertFriendRequestDataToModel(friendrequestData: FriendRequestData): FriendRequest {

      val recipient =
        User(
          username = friendrequestData.recipient.username,
          email = friendrequestData.recipient.email,
          password = friendrequestData.recipient.password
        )

      return FriendRequest(
        id = friendrequestData.id,
        status = friendrequestData.status,
        recipient = recipient
      )
    }

    fun convertMessageDataToModel(messageData: MessageData): Message {

      val sender =
        User(
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
      return users.map { user -> UserData(user.username, user.email, user.password) }
    }
  }
}
