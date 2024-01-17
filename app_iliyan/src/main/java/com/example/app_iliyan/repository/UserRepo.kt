package com.example.app_iliyan.repository

import android.util.Log
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepo : SharedRepo() {

  suspend fun handleModifyConfirmAuthUser(
    event: String,
    user: User,
    onResult: (String) -> Unit
  ): Boolean {
    return withContext(Dispatchers.Main) {
      try {
        val server: ServerResponse = sendUserData(event, user)

        if (server.status == "Success") {
          // Check if the event is Login, if so, set the authenticated user
          if (event == "Login") {
            LocalData.setAuthenticatedUser(
              server.data.user?.username.toString(),
              server.data.user?.email.toString(),
            )
          }
          onResult(server.message)
          return@withContext true
        } else {
          onResult(server.status + " : " + server.message)
        }
      } catch (e: Exception) {
        Log.e("Error", e.message.toString())
        onResult("Error, please try again later")
        return@withContext false
      }
      return@withContext false
    }
  }
}
