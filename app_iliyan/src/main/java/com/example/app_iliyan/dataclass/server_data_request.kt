package com.example.app_iliyan.dataclass

import kotlinx.serialization.Serializable

@Serializable sealed class EventData

@Serializable data class Event<T : EventData>(val eventType: String, val data: T)

@Serializable data class IdData(val id: String) : EventData()

@Serializable
data class CreateFriendRequestData(
  val emailSender: String,
  val status: String,
  val emailRecipient: String
) : EventData()

@Serializable
data class GroupChatData(val id: Int, val name: String, val users: List<UserData>) : EventData()

@Serializable
data class CreateMessageData(val message: MessageData, val groupchatid: String) : EventData()

@Serializable
data class UserData(val id: String, val username: String, val email: String, val password: String) :
  EventData()

@Serializable
data class MessageData(
  val id: String,
  val content: String,
  val attachmentURL: String,
  val timestamp: String,
  val sender: UserData
) : EventData()

@Serializable
data class FriendRequestData(val id: Int, val status: String, val recipient: UserData) :
  EventData()
