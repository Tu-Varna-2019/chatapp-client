package com.example.app_iliyan.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class FriendRequest {
  var id by mutableStateOf(0)
  var status by mutableStateOf("")
  var recipient by mutableStateOf(User(0, "", "", ""))
  var sender by mutableStateOf(User(0, "", "", ""))

  constructor(id: Int, status: String, recipient: User, sender: User) {
    this.id = id
    this.status = status
    this.recipient = recipient
    this.sender = sender
  }
}
