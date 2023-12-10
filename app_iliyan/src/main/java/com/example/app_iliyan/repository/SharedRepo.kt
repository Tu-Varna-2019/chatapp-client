package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.dataclass.UserData
import com.example.app_iliyan.dataclass.UserSignUpData
import com.example.app_iliyan.helpers.MaskData
import com.example.app_iliyan.model.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
}
