package com.example.app_iliyan.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Message {
  var id by mutableStateOf(0)
  var content by mutableStateOf("")
  var attachmentURL by mutableStateOf("")
  var timestamp by mutableStateOf("")
  var sender by mutableStateOf(User("", "", ""))

  constructor(id: Int, content: String, attachmentURL: String, timestamp: String, sender: User) {
    this.id = id
    this.content = content
    this.attachmentURL = attachmentURL
    this.timestamp = timestamp
    this.sender = sender
  }
}
