package com.example.app_iliyan.dataclass

import kotlinx.serialization.Serializable

@Serializable data class Event(val eventType: String, val data: EventData)

@Serializable
data class EventData(
  val id: String? = null,
  val emailSender: String? = null,
  val status: String? = null,
  val emailRecipient: String? = null,
  val groupchatid: String? = null,
  val groupchat: GroupChatData? = null,
  val user: UserData? = null,
  val message: MessageData? = null,
  val friendrequest: FriendRequestData? = null
)

@Serializable data class GroupChatData(val id: Int, val name: String, val users: List<UserData>)

@Serializable
data class UserData(val id: String, val username: String, val email: String, val password: String)

@Serializable
data class MessageData(
  val id: String,
  val content: String,
  val attachmentURL: String,
  val timestamp: String,
  val sender: UserData
)

@Serializable
data class FriendRequestData(
  val id: Int,
  val status: String,
  val recipient: UserData,
  val sender: UserData
)
