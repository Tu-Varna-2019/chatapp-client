package com.example.app_iliyan.view_model

import android.util.Log
import com.example.app_iliyan.dataclass.UserData
import com.example.app_iliyan.dataclass.UserSignUpData
import com.example.app_iliyan.helpers.MaskData
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.User
import com.example.app_iliyan.repository.SocketConnection
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserViewModel {

  companion object {
    @OptIn(DelicateCoroutinesApi::class)
    fun handleSignUpUserClick(user: User) {
      // && Utils.isValidPassword(user.password)
      if (Utils.isValidEmail(user.email)) {
        GlobalScope.launch(Dispatchers.Main) {
          try {
            val eventType: String = "SignUp"
            user.base64EncodeUser()
            val encodedEventType = MaskData.base64Encode(eventType)

            val userData = UserData(user.username, user.email, user.password)
            val userSignUpData = UserSignUpData(encodedEventType, userData)

            val jsonString = Json.encodeToString(userSignUpData)

            val response = SocketConnection.sendAndReceiveData(jsonString)
          } catch (e: Exception) {
            Log.e("SignUpError", e.message.toString())
          }
        }
      }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun handleLoginUserClick(user: User) {
      // && Utils.isValidPassword(user.password)
      if (Utils.isValidEmail(user.email)) {
        GlobalScope.launch(Dispatchers.Main) {
          try {
            val eventType: String = "SignUp"
            user.base64EncodeUser()
            val encodedEventType = MaskData.base64Encode(eventType)

            val userData = UserData(user.username, user.email, user.password)
            val userSignUpData = UserSignUpData(encodedEventType, userData)

            val jsonString = Json.encodeToString(userSignUpData)

            val response = SocketConnection.sendAndReceiveData(jsonString)
          } catch (e: Exception) {
            Log.e("SignUpError", e.message.toString())
          }
        }
      }
    }
  }
}
