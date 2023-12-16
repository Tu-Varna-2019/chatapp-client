package com.example.app_iliyan.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.Message
import com.example.app_iliyan.repository.ChatInterface
import com.example.app_iliyan.repository.ChatRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel(), ChatInterface {
  override val chatRepo: ChatRepo = ChatRepo()
  val backHomeEvent = MutableLiveData<Boolean>()

  private val _messages = MutableStateFlow<List<Message>>(emptyList())
  val messages: StateFlow<List<Message>> = _messages

  fun fetchMessages(groupChatID: Int) {
    viewModelScope.launch(Dispatchers.Main) {
      try {
        // Message
        val fetchMessages = chatRepo.messageRepo.getAllMessages(groupChatID)
        _messages.value = fetchMessages
      } catch (e: Exception) {
        Utils.logger.error("Error fetching messages!")
      }
    }
  }
}
