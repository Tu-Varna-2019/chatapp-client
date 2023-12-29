package com.example.app_iliyan.dataclass

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

@Serializable data class ServerResponse(val response: ResponseContent)

@Serializable
data class ResponseContent(
  val status: String,
  val message: String,
  val user: UserData? = null,
  val groupchats: List<GroupChatData>? = null,
  val messages: List<MessageData>? = null,
  val friendrequests: List<FriendRequestData>? = null
)

@OptIn(InternalSerializationApi::class)
inline fun <reified T : EventData> serializer(data: T) = T::class.serializer()
