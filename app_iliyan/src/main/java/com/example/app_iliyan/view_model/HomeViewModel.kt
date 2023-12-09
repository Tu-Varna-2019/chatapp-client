package com.example.app_iliyan.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.state.UserOptions
import com.example.app_iliyan.repository.ServerDataHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
  val logoutEvent = MutableLiveData<Boolean>()
  private val _groupChats = MutableStateFlow<List<GroupChat>>(emptyList())
  val groupChats: StateFlow<List<GroupChat>> = _groupChats

  private val _friendRequests = MutableStateFlow<List<FriendRequest>>(emptyList())
  val friendRequests: StateFlow<List<FriendRequest>> = _friendRequests

  fun fetchGroupChatsFriendRequests() {
    viewModelScope.launch(Dispatchers.Main) {
      try {
        // Group chat
        val fetchGroupChat = getAllGroupChatsAuthUser()
        _groupChats.value = fetchGroupChat
        // Friend request
        val fetchFriendRequest = getAllFriendRequestsAuthUser()
        _friendRequests.value = fetchFriendRequest
      } catch (e: Exception) {
        Utils.logger.error("HomeViewModel", "Error fetching group chats/friend requests!", e)
      }
    }
  }

  fun handleLogoutClick() {
    LocalData.setAuthenticatedUser(
      "",
      "",
    )
    logoutEvent.value = true
  }

  private suspend fun getAllGroupChatsAuthUser(): List<GroupChat> {
    try {
      val server: ServerResponse =
        UserViewModel.encodeAndSendUserDataByEvent(
          "GetGroupChatsAuthUser",
          LocalData.getAuthenticatedUser()!!
        )

      if (server.response.status == "Success" && server.response.groupchats != null) {

        val groupchatList =
          server.response.groupchats.map { groupChatData ->
            ServerDataHandler.convertGroupChatDataToModel(groupChatData.groupchat)
          }

        return groupchatList
      } else {
        Utils.logger.warn("getAllGroupChatsAuthUser: Not Chat Groups Found")
        return emptyList()
      }
    } catch (e: Exception) {
      Utils.logger.error("getAllGroupChatsAuthUser: {}", e.message.toString())
    }
    return emptyList()
  }

  private suspend fun getAllFriendRequestsAuthUser(): List<FriendRequest> {
    try {
      val server: ServerResponse =
        UserViewModel.encodeAndSendUserDataByEvent(
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

  companion object {
    fun handleSelectedTabClick(selectedTab: UserOptions) {
      when (selectedTab.selectedTab) {
        "Chat" -> {
          selectedTab.selectedTab = "Chat"
        }
        "Contacts" -> {
          selectedTab.selectedTab = "Contacts"
        }
        "Settings" -> {
          selectedTab.selectedTab = "Settings"
        }
      }
    }
  }
}
