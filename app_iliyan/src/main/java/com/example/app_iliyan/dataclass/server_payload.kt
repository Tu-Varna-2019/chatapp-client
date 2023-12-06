package com.example.app_iliyan.dataclass

import kotlinx.serialization.Serializable

@Serializable data class UserSignUpData(val eventType: String, val data: UserData)

@Serializable data class UserData(val username: String, val email: String, val password: String)

@Serializable data class GroupChat(val groupchat: GroupChatData)

@Serializable data class GroupChatData(val id: Int, val name: String, val users: List<UserData>)
