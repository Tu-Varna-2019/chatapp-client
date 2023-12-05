package com.example.app_iliyan.view_model

import com.example.app_iliyan.model.state.UserOptions

class HomeViewModel {
  companion object {
    fun handleSelectedTabClick(selectedTab: UserOptions) {
      when (selectedTab.selectedTab) {
        "Chat" -> {
          selectedTab.selectedTab = "Chat"
        }
        "Contacts" -> {
          selectedTab.selectedTab = "Contacts"
        }
        "Settings" -> {
            selectedTab.selectedTab = "Settings"
        }
      }
    }
  }
}
