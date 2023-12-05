package com.example.app_iliyan.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class FriendRequest {
    var status by mutableStateOf("")
    var sender by mutableStateOf(User("","",""))
    var receiver by mutableStateOf(User("","",""))

    constructor(status: String, sender: User, receiver: User) {
        this.status = status
        this.sender = sender
        this.receiver = receiver
    }
}
