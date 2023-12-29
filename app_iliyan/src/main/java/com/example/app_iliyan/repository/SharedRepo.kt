package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.CreateFriendRequestData
import com.example.app_iliyan.dataclass.CreateMessageData
import com.example.app_iliyan.dataclass.Event
import com.example.app_iliyan.dataclass.EventData
import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.dataclass.IdData
import com.example.app_iliyan.dataclass.MessageData
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.dataclass.UserData
import com.example.app_iliyan.dataclass.serializer
import com.example.app_iliyan.helpers.MaskData
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.model.User
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class SharedRepo {

  fun getEncodedUser(user: User): UserData {
    val encodedUserCreds = user.base64EncodeUser()

    return UserData(
      encodedUserCreds[0],
      encodedUserCreds[1],
      encodedUserCreds[2],
      encodedUserCreds[3]
    )
  }

  suspend inline fun <reified T : EventData> sendEncodedData(
    event: String,
    data: T
  ): ServerResponse {
    val encodedEventType = MaskData.base64Encode(event)
    val eventType = Event(encodedEventType, data)
    val jsonString = Json.encodeToString(Event.serializer(serializer(data)), eventType)

    val server: ServerResponse = SocketConnection.sendAndReceiveData(jsonString)

    return server
  }

  suspend fun sendUserData(event: String, user: User): ServerResponse {

    return sendEncodedData(event, getEncodedUser(user))
  }

  suspend fun sendMessageData(
    event: String,
    groupChatID: String,
    typedMessage: String,
    attachmentURL: String
  ): ServerResponse {

    val currentDateTime =
      LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
    val user = LocalData.getAuthenticatedUser() ?: User(0, "", "", "")

    val message =
      Message(
        id = 0,
        content = typedMessage,
        attachmentURL = attachmentURL,
        timestamp = currentDateTime,
        sender = user
      )
    val encodedMessage = message.base64EncodeMessage()

    val createMessageData =
      CreateMessageData(
        message =
          MessageData(
            encodedMessage[0],
            encodedMessage[1],
            encodedMessage[2],
            encodedMessage[3],
            getEncodedUser(message.sender)
          ),
        groupchatid = MaskData.base64Encode(groupChatID)
      )

    return sendEncodedData(event, createMessageData)
  }

  suspend fun SendID(event: String, id: Int): ServerResponse {

    val encodedID = MaskData.base64Encode(id.toString())
    val IdData = IdData(encodedID)

    return sendEncodedData(event, IdData)
  }

  suspend fun sendAddFriendRequest(event: String, friendRequest: FriendRequest): ServerResponse {

    val friendRequestData =
      CreateFriendRequestData(
        MaskData.base64Encode(friendRequest.sender.email),
        MaskData.base64Encode(friendRequest.status),
        MaskData.base64Encode(friendRequest.recipient.email)
      )

    return sendEncodedData(event, friendRequestData)
  }

  suspend fun sendGroupChat(event: String, groupChat: GroupChat): ServerResponse {

    val groupChatData =
      GroupChatData(
        groupChat.id,
        MaskData.base64Encode(groupChat.name),
        groupChat.users.map { user -> getEncodedUser(user) }
      )

    return sendEncodedData(event, groupChatData)
  }
}
