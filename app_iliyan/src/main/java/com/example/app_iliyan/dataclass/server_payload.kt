package com.example.app_iliyan.dataclass

import kotlinx.serialization.Serializable

@Serializable data class IdEventData(val eventType: String, val data: IdData)

@Serializable data class CreateMessageEvent(val eventType: String, val data: CreateMessageData)

@Serializable data class CreateMessageData(val message: MessageData, val groupchatid: String)

@Serializable data class IdData(val id: String)

@Serializable data class UserSignUpData(val eventType: String, val data: UserData)

@Serializable data class UserData(val username: String, val email: String, val password: String)

@Serializable data class GroupChatDataClass(val groupchat: GroupChatData)

@Serializable data class MessageDataClass(val message: MessageData)

@Serializable data class GroupChatData(val id: Int, val name: String, val users: List<UserData>)

@Serializable
data class MessageData(
  val id: String,
  val content: String,
  val attachmentURL: String,
  val timestamp: String,
  val sender: UserData
)

@Serializable data class FriendRequestDataClass(val friendrequest: FriendRequestData)

@Serializable
data class FriendRequestData(val id: Int, val status: String, val recipient: UserData)
