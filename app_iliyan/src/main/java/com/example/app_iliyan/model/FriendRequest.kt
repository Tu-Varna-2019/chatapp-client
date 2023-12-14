package com.example.app_iliyan.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class FriendRequest {
  var id by mutableStateOf(0)
  var status by mutableStateOf("")
  var recipient by mutableStateOf(User("", "", ""))

  constructor(id: Int, status: String, recipient: User) {
    this.id = id
    this.status = status
    this.recipient = recipient
  }
}
