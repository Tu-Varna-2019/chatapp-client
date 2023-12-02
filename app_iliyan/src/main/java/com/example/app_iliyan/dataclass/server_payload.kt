package com.example.app_iliyan.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpData(
    val eventType: String,
    val data: UserData
)

@Serializable
data class UserData(
    val username: String,
    val email: String,
    val password: String
)
