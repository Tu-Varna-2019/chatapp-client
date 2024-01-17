package com.example.app_iliyan.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.model.LocalData
import com.example.app_iliyan.model.User
import com.example.app_iliyan.model.state.UserOptions
import com.example.app_iliyan.repository.ChatInterface
import com.example.app_iliyan.repository.ChatRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel(), ChatInterface {
  override val chatRepo: ChatRepo = ChatRepo()
  val logoutEvent = MutableLiveData<Boolean>()
  val gotoMessageEvent = MutableLiveData<GroupChat?>()
  private val _groupChats = MutableStateFlow<List<GroupChat>>(emptyList())
  val groupChats: StateFlow<List<GroupChat>> = _groupChats

  fun fetchGroupChats() {
    viewModelScope.launch(Dispatchers.Main) {
      while (isActive) {
        try {
          // Group chat
          val fetchGroupChat = chatRepo.groupChatRepo.getAllGroupChats()
          _groupChats.value = fetchGroupChat
        } catch (e: Exception) {
          Utils.logger.error("HomeViewModel", "Error fetching group chats/friend requests!", e)
        }
        delay(Utils.SERVER_REQUEST_DELAY)
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

  fun handleGotoMessageClick(groupChat: GroupChat) {
    gotoMessageEvent.value = groupChat
  }

  suspend fun handleDeleteGroupChatClick(groupchatid: Int): String {
    val deleteGroupChatMessage = chatRepo.groupChatRepo.handleDeleteGroupChat(groupchatid)

    return deleteGroupChatMessage
  }

  suspend fun handleDeleteAccountClick(userEnteredPassword: String, onResult: (String) -> Unit) {
    val user =
      User(
        0,
        LocalData.getAuthenticatedUser()?.username ?: "",
        LocalData.getAuthenticatedUser()?.email ?: "",
        userEnteredPassword
      )
    val isDeleteAccountSuccess =
      chatRepo.userRepo.handleModifyConfirmAuthUser("DeleteAccount", user, onResult)

    if (isDeleteAccountSuccess) logoutEvent.value = true
  }

  suspend fun handleRenameUsernameClick(userEnteredUsername: String, onResult: (String) -> Unit) {
    val user =
      User(
        0,
        userEnteredUsername,
        LocalData.getAuthenticatedUser()?.email ?: "",
        LocalData.getAuthenticatedUser()?.password ?: ""
      )
    val isUsernameRenamed =
      chatRepo.userRepo.handleModifyConfirmAuthUser("RenameUsername", user, onResult)

    if (isUsernameRenamed)
      LocalData.setAuthenticatedUser(
        userEnteredUsername,
        LocalData.getAuthenticatedUser()?.email ?: "",
      )
  }

  suspend fun handleRenameEmailClick(userEnteredEmail: String, onResult: (String) -> Unit) {
    val user =
      User(
        0,
        LocalData.getAuthenticatedUser()?.email ?: "",
        userEnteredEmail,
        LocalData.getAuthenticatedUser()?.password ?: ""
      )

    val isEmailRenamed =
      chatRepo.userRepo.handleModifyConfirmAuthUser("RenameEmail", user, onResult)

    if (isEmailRenamed)
      LocalData.setAuthenticatedUser(
        LocalData.getAuthenticatedUser()?.username ?: "",
        userEnteredEmail,
      )
  }

  suspend fun handleChangePasswordClick(
    oldPassword: String,
    newPassword: String,
    onResult: (String) -> Unit
  ) {
    val user = User(0, oldPassword, LocalData.getAuthenticatedUser()?.email ?: "", newPassword)

    val isPasswordChanged =
      chatRepo.userRepo.handleModifyConfirmAuthUser("ChangePassword", user, onResult)

    if (isPasswordChanged) logoutEvent.value = true
  }

  suspend fun handleSendFriendRequestClick(recipientEmail: String, onResult: (String) -> Unit) {
    chatRepo.friendRequestRepo.handleSendFriendRequest(recipientEmail, onResult)
  }

  suspend fun handleCreateGroupChatClick(groupchatName: String, onResult: (String) -> Unit) {
    chatRepo.groupChatRepo.handleCreateGroupChat(groupchatName, onResult)
  }

  companion object {
    fun handleSelectedTabClick(selectedTab: UserOptions) {
      when (selectedTab.selectedTab) {
        "Chat" -> {
          selectedTab.selectedTab = "Chat"
        }
        "Friends" -> {
          selectedTab.selectedTab = "Friends"
        }
        "Settings" -> {
          selectedTab.selectedTab = "Settings"
        }
      }
    }
  }
}
