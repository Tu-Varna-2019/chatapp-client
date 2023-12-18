package com.example.app_iliyan.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class GroupChat {
  var id by mutableStateOf(0)
  var name by mutableStateOf("")
  var users by mutableStateOf(listOf<User>())
  var messages by mutableStateOf(listOf<Message>())

  constructor(id: Int, name: String, users: List<User>, messages: List<Message> = listOf()) {
    this.id = id
    this.name = name
    this.users = users
    this.messages = messages
  }
}
