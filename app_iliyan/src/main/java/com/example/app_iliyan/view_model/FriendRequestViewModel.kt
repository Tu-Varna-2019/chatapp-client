package com.example.app_iliyan.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_iliyan.dataclass.FilterRequest
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.FriendRequest
import com.example.app_iliyan.repository.ChatInterface
import com.example.app_iliyan.repository.ChatRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FriendRequestViewModel : ViewModel(), ChatInterface {
  override val chatRepo: ChatRepo = ChatRepo()

  private val _friendRequests = MutableStateFlow<List<FriendRequest>>(emptyList())
  val friendRequests: StateFlow<List<FriendRequest>> = _friendRequests

  fun fetchFriendRequests(filterRequest: FilterRequest? = null) {
    viewModelScope.launch(Dispatchers.Main) {
      while (isActive) {
        try {
          // Friend request
          val fetchFriendRequest = chatRepo.friendRequestRepo.getAllFriendRequests(filterRequest)
          _friendRequests.value = fetchFriendRequest
        } catch (e: Exception) {
          Utils.logger.error(
            "fetchFriendRequests",
            "Error fetching group chats/friend requests!",
            e
          )
        }
        delay(Utils.SERVER_REQUEST_DELAY)
      }
    }
  }

  suspend fun handleDeleteFriendRequestClick(
    event: String,
    deletedFriendRequest: FriendRequest
  ): String {

    val friendRequestDeleteMessage: String =
      chatRepo.friendRequestRepo.handleFriendRequestOperation(event, deletedFriendRequest)

    // Remove deleted friend request if no exception is raised
    if (!friendRequestDeleteMessage.isEmpty())
      _friendRequests.value = _friendRequests.value.filterNot { it.id == deletedFriendRequest.id }

    return friendRequestDeleteMessage
  }

  suspend fun handleFriendRequestOperation(
    event: String,
    updatedFriendRequestStatus: FriendRequest
  ): String {

    val friendRequestUpdateMessage: String =
      chatRepo.friendRequestRepo.handleFriendRequestOperation(event, updatedFriendRequestStatus)

    // Set updated status friend request if no exception is raised
    if (!friendRequestUpdateMessage.isEmpty())
      _friendRequests.value =
        _friendRequests.value.map { friendRequest ->
          if (friendRequest.id == updatedFriendRequestStatus.id) {
            updatedFriendRequestStatus
          } else {
            friendRequest
          }
        }

    return friendRequestUpdateMessage
  }
}
