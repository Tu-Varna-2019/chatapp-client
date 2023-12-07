package com.example.app_iliyan.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class FriendRequest {
  var status by mutableStateOf("")
  var recipient by mutableStateOf(User("", "", ""))

  constructor(status: String, recipient: User) {
    this.status = status
    this.recipient = recipient
  }
}
