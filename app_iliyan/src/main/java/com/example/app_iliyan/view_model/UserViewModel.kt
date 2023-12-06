package com.example.app_iliyan.view_model

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.dataclass.UserData
import com.example.app_iliyan.dataclass.UserSignUpData
import com.example.app_iliyan.helpers.MaskData
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.User
import com.example.app_iliyan.repository.SocketConnection
import com.example.app_iliyan.view.HomeActivity
import com.example.app_iliyan.view.LoginActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserViewModel {

  companion object {
    suspend fun encodeAndSendUserDataByEvent(event: String, user: User): ServerResponse {

      val encodedUserCreds = user.base64EncodeUser()
      val encodedEventType = MaskData.base64Encode(event)

      val userData = UserData(encodedUserCreds[0], encodedUserCreds[1], encodedUserCreds[2])
      val userSignUpData = UserSignUpData(encodedEventType, userData)
      val jsonString = Json.encodeToString(userSignUpData)

      val server: ServerResponse = SocketConnection.sendAndReceiveData(jsonString)

      return server
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun handleSignUpUserClick(user: User, context: Context, onResult: (String) -> Unit) {
      GlobalScope.launch(Dispatchers.Main) {
        try {
          val server: ServerResponse = encodeAndSendUserDataByEvent("SignUp", user)

          if (server.response.status == "Success") {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            onResult(server.response.message)
          } else {
            onResult(server.response.status + " : " + server.response.message)
          }
        } catch (e: Exception) {
          Log.e("SignUpError", e.message.toString())
          onResult("Error, please try again later")
        }
      }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun handleLoginUserClick(user: User, context: Context, onResult: (String) -> Unit) {
      GlobalScope.launch(Dispatchers.Main) {
        try {
          val server: ServerResponse = encodeAndSendUserDataByEvent("Login", user)

          if (server.response.status == "Success") {

            LocalData.setAuthenticatedUser(
                server.response.user?.username.toString(),
                server.response.user?.email.toString(),
            )

            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
            onResult(server.response.message)
          } else {
            onResult(server.response.status + " : " + server.response.message)
          }
        } catch (e: Exception) {
          Log.e("LoginError", e.message.toString())
          onResult("Error, please try again later")
        }
      }
    }
  }
}
