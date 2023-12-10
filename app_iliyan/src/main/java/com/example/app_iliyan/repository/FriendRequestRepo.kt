package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.LocalData

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
}
