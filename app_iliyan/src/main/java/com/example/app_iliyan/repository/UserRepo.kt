package com.example.app_iliyan.repository

import android.util.Log
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepo : SharedRepo() {
  suspend fun handleSignUpUserClick(user: User, onResult: (String) -> Unit): Boolean {
    return withContext(Dispatchers.Main) {
      try {
        val server: ServerResponse = encodeAndSendUserDataByEvent("SignUp", user)

        if (server.response.status == "Success") {
          onResult(server.response.message)
          return@withContext true
        } else {
          onResult(server.response.status + " : " + server.response.message)
        }
      } catch (e: Exception) {
        Log.e("SignUpError", e.message.toString())
        onResult("Error, please try again later")
        return@withContext false
      }
      return@withContext false
    }
  }

  suspend fun handleLoginUserClick(user: User, onResult: (String) -> Unit): Boolean {
    return withContext(Dispatchers.Main) {
      try {
        val server: ServerResponse = encodeAndSendUserDataByEvent("Login", user)

        if (server.response.status == "Success") {

          LocalData.setAuthenticatedUser(
            server.response.user?.username.toString(),
            server.response.user?.email.toString(),
          )
          onResult(server.response.message)
          return@withContext true
        } else {
          onResult(server.response.status + " : " + server.response.message)
        }
      } catch (e: Exception) {
        Log.e("LoginError", e.message.toString())
        onResult("Error, please try again later")
        return@withContext false
      }
      return@withContext false
    }
  }

  suspend fun handleDeleteAuthUser(
    userEnteredPassword: String,
    onResult: (String) -> Unit
  ): Boolean {
    return withContext(Dispatchers.Main) {
      try {
        val user =
          User(
            LocalData.getAuthenticatedUser()?.username ?: "",
            LocalData.getAuthenticatedUser()?.email ?: "",
            userEnteredPassword
          )
        val server: ServerResponse = encodeAndSendUserDataByEvent("DeleteAccount", user)

        if (server.response.status == "Success") {

          onResult(server.response.message)
          return@withContext true
        } else {
          onResult(server.response.status + " : " + server.response.message)
          return@withContext false
        }
      } catch (e: Exception) {
        Log.e("DeleteAccountError", e.message.toString())
        onResult("Error, please try again later")
        return@withContext false
      }
      return@withContext false
    }
  }

  suspend fun handleRenameUsernameAuthUser(
    userEnteredUsername: String,
    onResult: (String) -> Unit
  ): Boolean {
    return withContext(Dispatchers.Main) {
      try {
        val user =
          User(
            userEnteredUsername,
            LocalData.getAuthenticatedUser()?.email ?: "",
            LocalData.getAuthenticatedUser()?.password ?: ""
          )
        val server: ServerResponse = encodeAndSendUserDataByEvent("RenameUsername", user)

        if (server.response.status == "Success") {
          onResult(server.response.message)
          LocalData.setAuthenticatedUser(
            userEnteredUsername,
            server.response.user?.email.toString(),
          )
          return@withContext true
        } else {
          onResult(server.response.status + " : " + server.response.message)
          return@withContext false
        }
      } catch (e: Exception) {
        Log.e("RenameUsernameError", e.message.toString())
        onResult("Error, please try again later")
        return@withContext false
      }
      return@withContext false
    }
  }
}
