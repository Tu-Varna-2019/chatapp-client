package com.example.app_iliyan.model.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class UserOptions {
  var searchText by mutableStateOf("")
  var selectedTab by mutableStateOf("Chat")

    constructor(searchText: String, selectedTab: String) {
        this.searchText = searchText
        this.selectedTab = selectedTab
    }
}
