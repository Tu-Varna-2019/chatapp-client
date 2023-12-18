package com.example.app_iliyan.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.GroupChat
import com.example.app_iliyan.repository.ChatInterface
import com.example.app_iliyan.repository.ChatRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel(), ChatInterface {
  override val chatRepo: ChatRepo = ChatRepo()
  val backHomeEvent = MutableLiveData<Boolean>()

  private val _selectedGroupChat = MutableStateFlow<GroupChat>(GroupChat(0, "", emptyList()))
  val selectedGroupChat: StateFlow<GroupChat> = _selectedGroupChat

  fun fetchMessagesByGroupChat(groupChatDataArg: GroupChatData) {
    viewModelScope.launch(Dispatchers.Main) {
      try {
        // Message
        val fetchMessages = chatRepo.messageRepo.getAllMessages(groupChatDataArg)
        _selectedGroupChat.value = fetchMessages
      } catch (e: Exception) {
        Utils.logger.error("Error fetching messages!")
      }
    }
  }
}
