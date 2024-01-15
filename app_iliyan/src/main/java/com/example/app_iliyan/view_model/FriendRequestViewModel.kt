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

  suspend fun handleDeleteFriendRequestClick(deletedFriendRequest: FriendRequest) {
    val isFriendRequestDeleted =
      chatRepo.friendRequestRepo.deleteFriendRequest(deletedFriendRequest.id)

    if (isFriendRequestDeleted)
      _friendRequests.value = _friendRequests.value.filterNot { it.id == deletedFriendRequest.id }
  }
}
