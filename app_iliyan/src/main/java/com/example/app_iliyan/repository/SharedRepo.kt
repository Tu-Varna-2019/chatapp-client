package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.CreateMessageData
import com.example.app_iliyan.dataclass.CreateMessageEvent
import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.dataclass.IdData
import com.example.app_iliyan.dataclass.IdEventData
import com.example.app_iliyan.dataclass.MessageData
import com.example.app_iliyan.dataclass.ReqCreateGroupChatEvent
import com.example.app_iliyan.dataclass.ReqFriendRequestData
import com.example.app_iliyan.dataclass.ReqFriendRequestEvent
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.dataclass.UserData
import com.example.app_iliyan.dataclass.UserSignUpData
import com.example.app_iliyan.helpers.MaskData
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.model.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class SharedRepo {
  suspend fun encodeAndSendUserDataByEvent(event: String, user: User): ServerResponse {

    val encodedUserCreds = user.base64EncodeUser()
    val encodedEventType = MaskData.base64Encode(event)

    val userData = UserData(encodedUserCreds[0], encodedUserCreds[1], encodedUserCreds[2])
    val userSignUpData = UserSignUpData(encodedEventType, userData)
    val jsonString = Json.encodeToString(userSignUpData)

    val server: ServerResponse = SocketConnection.sendAndReceiveData(jsonString)

    return server
  }

  suspend fun encodeAndSendMessageDataByEvent(
    event: String,
    groupChatID: String,
    typedMessage: String,
    attachmentURL: String
  ): ServerResponse {

    val currentDateTime =
      LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
    val user = LocalData.getAuthenticatedUser() ?: User("", "", "")

    val message =
      Message(
        id = 0,
        content = typedMessage,
        attachmentURL = attachmentURL,
        timestamp = currentDateTime,
        sender = user
      )

    val encodedSender = message.sender.base64EncodeUser()
    val encodedMessage = message.base64EncodeMessage()
    val encodedEventType = MaskData.base64Encode(event)

    val messageData =
      CreateMessageData(
        message =
          MessageData(
            encodedMessage[0],
            encodedMessage[1],
            encodedMessage[2],
            encodedMessage[3],
            UserData(encodedSender[0], encodedSender[1], encodedSender[2])
          ),
        groupchatid = MaskData.base64Encode(groupChatID)
      )

    val createMessageData =
      CreateMessageEvent(
        encodedEventType,
        messageData,
      )
    val jsonString = Json.encodeToString(createMessageData)
    Utils.logger.error("jsonString: {}", jsonString)
    val server: ServerResponse = SocketConnection.sendAndReceiveData(jsonString)

    return server
  }

  suspend fun encodeAndSendIDByEvent(event: String, id: Int): ServerResponse {

    val encodedID = MaskData.base64Encode(id.toString())
    val encodedEventType = MaskData.base64Encode(event)

    val IdData = IdData(encodedID)
    val IdEventData = IdEventData(encodedEventType, IdData)

    val jsonString = Json.encodeToString(IdEventData)

    val server: ServerResponse = SocketConnection.sendAndReceiveData(jsonString)

    return server
  }

  suspend fun encodeAndSendAddFriendRequestByEvent(
    event: String,
    friendRequest: FriendRequest
  ): ServerResponse {

    val encodedEventType = MaskData.base64Encode(event)

    val friendRequestData =
      ReqFriendRequestData(
        MaskData.base64Encode(friendRequest.sender.email),
        MaskData.base64Encode(friendRequest.status),
        MaskData.base64Encode(friendRequest.recipient.email)
      )

    val friendRequestEvent = ReqFriendRequestEvent(encodedEventType, friendRequestData)

    val jsonString = Json.encodeToString(friendRequestEvent)

    val server: ServerResponse = SocketConnection.sendAndReceiveData(jsonString)

    return server
  }

  suspend fun encodeAndSendGroupChatByEvent(event: String, groupChat: GroupChat): ServerResponse {

    val encodedEventType = MaskData.base64Encode(event)

    val groupChatData =
      GroupChatData(
        groupChat.id,
        MaskData.base64Encode(groupChat.name),
        groupChat.users.map { user ->
          UserData(
            MaskData.base64Encode(user.username),
            MaskData.base64Encode(user.email),
            MaskData.base64Encode(user.password)
          )
        }
      )

    val groupChatEvent = ReqCreateGroupChatEvent(encodedEventType, groupChatData)

    val jsonString = Json.encodeToString(groupChatEvent)

    val server: ServerResponse = SocketConnection.sendAndReceiveData(jsonString)

    return server
  }
}
