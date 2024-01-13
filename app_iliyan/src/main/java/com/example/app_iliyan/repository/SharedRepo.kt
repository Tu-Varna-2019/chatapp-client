package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.Event
import com.example.app_iliyan.dataclass.EventData
import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.dataclass.MessageData
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class SharedRepo {

  private fun convertToUserData(user: User): UserData {

    return UserData(user.id.toString(), user.username, user.email, user.password)
  }

  private fun convertToMessageData(message: Message): MessageData {

    return MessageData(
      message.id.toString(),
      message.content,
      message.attachmentURL,
      message.timestamp,
      convertToUserData(message.sender)
    )
  }

  private suspend inline fun <reified T : EventData> sendClientData(
    event: String,
    data: T
  ): ServerResponse {

    val eventData = EventData::class.java.newInstance()

    EventData::class.java.declaredFields.forEach { field ->
      if (field.type.isAssignableFrom(data::class.java)) {
        field.isAccessible = true
        field.set(eventData, data)
      }
    }

    val jsonElement = Json.encodeToJsonElement(data)
    val decodedData = Json.decodeFromJsonElement<T>(jsonElement)
    val eventType = Event(event, decodedData)
    val jsonString = Json.encodeToString(Event.serializer(), eventType)

    val server: ServerResponse = SocketConnection.sendAndReceiveData(jsonString)

    return server
  }

  suspend fun sendUserData(event: String, user: User): ServerResponse {

    return sendClientData(event, EventData(user = convertToUserData(user)))
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
      EventData(message = convertToMessageData(message), groupchatid = groupChatID)
    )
  }

  suspend fun SendID(event: String, id: Int): ServerResponse {

    return sendClientData(event, EventData(id = id.toString()))
  }

  suspend fun sendAddFriendRequest(event: String, friendRequest: FriendRequest): ServerResponse {

    return sendClientData(
      event,
      EventData(
        status = friendRequest.status,
        emailRecipient = friendRequest.recipient.email,
        emailSender = friendRequest.sender.email
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

    return sendClientData(event, EventData(groupchat = groupChatData))
  }
}
