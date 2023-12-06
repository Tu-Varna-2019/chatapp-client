package com.example.app_iliyan.dataclass

import kotlinx.serialization.Serializable

@Serializable data class ServerResponse(val response: ResponseContent)

@Serializable
data class ResponseContent(
    val status: String,
    val message: String,
    val user: UserData? = null,
    val groupchats: List<GroupChatDataClass>? = null
)
