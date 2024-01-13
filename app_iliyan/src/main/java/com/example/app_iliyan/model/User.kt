package com.example.app_iliyan.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class User {
  var id by mutableStateOf(0)
  var username by mutableStateOf("")
  var email by mutableStateOf("")
  var password by mutableStateOf("")

  constructor(id: Int, username: String, email: String, password: String) {
    this.id = id
    this.username = username
    this.email = email
    this.password = password
  }

  override fun toString(): String {
    return "User(id='$id',username='$username', email='$email', password='$password')"
  }
}
