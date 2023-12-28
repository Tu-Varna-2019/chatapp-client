package com.example.app_iliyan.repository

import android.util.Log
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendRequestRepo : SharedRepo() {
  suspend fun getAllFriendRequestsAuthUser(): List<FriendRequest> {
    try {
      val server: ServerResponse =
        encodeAndSendUserDataByEvent(
          "GetFriendRequestsAuthUser",
          LocalData.getAuthenticatedUser()!!
        )

      if (server.response.status == "Success" && server.response.friendrequests != null) {

        val friendrequestList =
          server.response.friendrequests.map { friendrequestData ->
            ServerDataHandler.convertFriendRequestDataToModel(friendrequestData.friendrequest)
          }

        return friendrequestList
      } else {
        Utils.logger.warn("GetFriendRequestsAuthUser: No Friend requests Found")
        return emptyList()
      }
    } catch (e: Exception) {
      Utils.logger.error("getAllGroupChatsAuthUser: {}", e.message.toString())
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
            recipient = User(username = "", email = recipientEmail, password = "")
          )
        val server: ServerResponse =
          encodeAndSendAddFriendRequestByEvent("SendFriendRequest", friendRequest)

        if (server.response.status == "Success") {

          onResult(server.response.message)
          return@withContext true
        } else {
          onResult(server.response.status + " : " + server.response.message)
          return@withContext false
        }
      } catch (e: Exception) {
        Log.e("SendFriendRequest", e.message.toString())
        onResult("Error, please try again later")
        return@withContext false
      }
    }
  }
}
