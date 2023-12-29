package com.example.app_iliyan.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.app_iliyan.helpers.MaskData

class User {
  var id by mutableStateOf(0)
  var username by mutableStateOf("")
  var email by mutableStateOf("")
  var password by mutableStateOf("")

  fun base64EncodeUser(): Array<String> {
    return arrayOf(
      MaskData.base64Encode(id.toString()),
      MaskData.base64Encode(username),
      MaskData.base64Encode(email),
      MaskData.base64Encode(password)
    )
  }

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
