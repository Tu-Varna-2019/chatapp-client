package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.DataRequest
import com.example.app_iliyan.dataclass.FilterRequest
import com.example.app_iliyan.dataclass.FriendRequestData
import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.dataclass.MessageData
import com.example.app_iliyan.dataclass.ServerRequest
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.dataclass.UserData
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.model.User
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class SharedRepo {

  private fun convertToUserData(user: User): UserData {

    return UserData(user.id, user.username, user.email, user.password)
  }

  private fun convertToMessageData(message: Message): MessageData {

    return MessageData(
      message.id,
      message.content,
      message.attachmentURL,
      Timestamp.valueOf(message.timestamp),
      convertToUserData(message.sender)
    )
  }

  private suspend inline fun <reified T : DataRequest> sendClientData(
    event: String,
    data: T,
    filter: FilterRequest? = null
  ): ServerResponse {

    val eventData = DataRequest::class.java.newInstance()

    DataRequest::class.java.declaredFields.forEach { field ->
      if (field.type.isAssignableFrom(data::class.java)) {
        field.isAccessible = true
        field.set(eventData, data)
      }
    }

    val jsonElement = Json.encodeToJsonElement(data)
    val decodedData = Json.decodeFromJsonElement<T>(jsonElement)

    val eventType = ServerRequest(event, decodedData, filter)
    val jsonString = Json.encodeToString(ServerRequest.serializer(), eventType)

    val server: ServerResponse = SocketConnection.sendAndReceiveData(jsonString)

    return server
  }

  suspend fun sendUserData(event: String, user: User): ServerResponse {

    return sendClientData(
      event,
      data = DataRequest(user = convertToUserData(user)),
      // filter = FilterRequest(friendrequest = filterFriendRequest)
    )
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

    return sendClientData(
      event,
      DataRequest(message = convertToMessageData(message), id = groupChatID)
    )
  }

  suspend fun sendIDData(event: String, id: Int): ServerResponse {

    return sendClientData(event, DataRequest(id = id.toString()))
  }

  suspend fun sendAddFriendRequest(event: String, friendRequest: FriendRequest): ServerResponse {

    return sendClientData(
      event,
      DataRequest(
        friendrequest =
          FriendRequestData(
            friendRequest.id,
            friendRequest.status,
            convertToUserData(friendRequest.recipient),
            convertToUserData(friendRequest.sender)
          )
      )
    )
  }

  suspend fun sendGroupChat(event: String, groupChat: GroupChat): ServerResponse {

    val groupChatData =
      GroupChatData(
        groupChat.id,
        groupChat.name,
        groupChat.users.map { user -> convertToUserData(user) }
      )

    return sendClientData(event, DataRequest(groupchat = groupChatData))
  }
}
