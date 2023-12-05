package com.example.app_iliyan.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class GroupChat {
  var name by mutableStateOf("")
  var users by mutableStateOf(listOf<User>())

  constructor(name: String, users: List<User>) {
    this.name = name
    this.users = users
  }
}
