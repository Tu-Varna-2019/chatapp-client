package com.example.app_iliyan.repository

import android.util.Log
import com.example.app_iliyan.dataclass.FilterRequest
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendRequestRepo : SharedRepo() {
  suspend fun getAllFriendRequests(filterRequest: FilterRequest? = null): List<FriendRequest> {

    try {
      val server: ServerResponse =
        sendUserData(
          event = "GetFriendRequests",
          user = LocalData.getAuthenticatedUser()!!,
          filterRequest = filterRequest
        )

      if (server.status == "Success" && server.data.friendrequests != null) {

        val friendrequestList =
          server.data.friendrequests.map { friendrequestData ->
            ServerDataHandler.convertFriendRequestDataToModel(friendrequestData)
          }

        return friendrequestList
      } else {
        Utils.logger.warn("GetFriendRequests: No Friend requests Found")
        return emptyList()
      }
    } catch (e: Exception) {
      Utils.logger.error("getAllGroupChats: {}", e.message.toString())
    }
    return emptyList()
  }

  suspend fun handleSendFriendRequest(recipientEmail: String, onResult: (String) -> Unit): Boolean {
    return withContext(Dispatchers.Main) {
      try {
        val friendRequest =
          FriendRequest(
            id = 0,
            status = "Pending",
            sender = LocalData.getAuthenticatedUser()!!,
            recipient = User(0, username = "", email = recipientEmail, password = "")
          )
        val server: ServerResponse = sendFriendRequest("SendFriendRequest", friendRequest)

        if (server.status == "Success") {

          onResult(server.message)
          return@withContext true
        } else {
          onResult(server.status + " : " + server.message)
          return@withContext false
        }
      } catch (e: Exception) {
        Log.e("SendFriendRequest", e.message.toString())
        onResult("Error, please try again later")
        return@withContext false
      }
    }
  }

  suspend fun handleFriendRequestOperation(event: String, friendRequest: FriendRequest): String {
    try {
      val server: ServerResponse = sendFriendRequest(event, friendRequest)

      return server.message
    } catch (e: Exception) {
      Utils.logger.error("friendRequestOperation: {}", e.message.toString())
    }
    return ""
  }
}
