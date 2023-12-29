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
        val server: ServerResponse = sendUserData("SignUp", user)

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
        val server: ServerResponse = sendUserData("Login", user)

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
            0,
            LocalData.getAuthenticatedUser()?.username ?: "",
            LocalData.getAuthenticatedUser()?.email ?: "",
            userEnteredPassword
          )
        val server: ServerResponse = sendUserData("DeleteAccount", user)

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
            0,
            userEnteredUsername,
            LocalData.getAuthenticatedUser()?.email ?: "",
            LocalData.getAuthenticatedUser()?.password ?: ""
          )
        val server: ServerResponse = sendUserData("RenameUsername", user)

        if (server.response.status == "Success") {
          onResult(server.response.message)
          LocalData.setAuthenticatedUser(
            userEnteredUsername,
            LocalData.getAuthenticatedUser()?.email ?: "",
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
    }
  }

  suspend fun handleRenameEmailAuthUser(
    userEnteredEmail: String,
    onResult: (String) -> Unit
  ): Boolean {
    return withContext(Dispatchers.Main) {
      try {
        // Sent userdata is in the format User (old-email, new-email, empty password)
        // Reason: avoid creating new dataclass for this purpose
        val user =
          User(
            0,
            LocalData.getAuthenticatedUser()?.email ?: "",
            userEnteredEmail,
            LocalData.getAuthenticatedUser()?.password ?: ""
          )

        val server: ServerResponse = sendUserData("RenameEmail", user)

        if (server.response.status == "Success") {

          onResult(server.response.message)
          LocalData.setAuthenticatedUser(
            LocalData.getAuthenticatedUser()?.username ?: "",
            userEnteredEmail,
          )
          return@withContext true
        } else {
          onResult(server.response.status + " : " + server.response.message)
          return@withContext false
        }
      } catch (e: Exception) {
        Log.e("RenameEmailError", e.message.toString())
        onResult("Error, please try again later")
        return@withContext false
      }
    }
  }

  suspend fun handleChangePasswordAuthUser(
    userEnteredOldPassword: String,
    userEnteredNewPassword: String,
    onResult: (String) -> Unit
  ): Boolean {
    return withContext(Dispatchers.Main) {
      try {
        // Sent userdata is in the format User (new-password, new-email, old-password)
        // Reason: avoid creating new dataclass for this purpose
        val user =
          User(
            0,
            userEnteredNewPassword,
            LocalData.getAuthenticatedUser()?.email ?: "",
            userEnteredOldPassword
          )

        val server: ServerResponse = sendUserData("ChangePassword", user)

        if (server.response.status == "Success") {

          onResult(server.response.message)

          return@withContext true
        } else {
          onResult(server.response.status + " : " + server.response.message)
          return@withContext false
        }
      } catch (e: Exception) {
        Log.e("ChangePasswordError", e.message.toString())
        onResult("Error, please try again later")
        return@withContext false
      }
    }
  }
}
