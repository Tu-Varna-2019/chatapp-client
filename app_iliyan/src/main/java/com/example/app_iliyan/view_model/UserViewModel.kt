package com.example.app_iliyan.view_model

import androidx.lifecycle.ViewModel
import com.example.app_iliyan.model.User
import com.example.app_iliyan.repository.ChatInterface
import com.example.app_iliyan.repository.ChatRepo

class UserViewModel : ViewModel(), ChatInterface {
  override val chatRepo: ChatRepo = ChatRepo()

  suspend fun handleRemoveUserFromGroupChatClick(
    removedUserFromGroupChat: User,
    groupChatId: Int
  ): String {
    val userRemovedFromGroupChatMessage =
      chatRepo.groupChatRepo.removeUserFromGroupChat(removedUserFromGroupChat, groupChatId)

    return userRemovedFromGroupChatMessage
  }

  suspend fun handleAddUserFromGroupChatClick(
    addedUserToGroupChat: User,
    groupChatId: Int
  ): String {
    val userAddedToGroupChatMessage =
      chatRepo.groupChatRepo.addUserFromGroupChat(addedUserToGroupChat, groupChatId)

    return userAddedToGroupChatMessage
  }
}
