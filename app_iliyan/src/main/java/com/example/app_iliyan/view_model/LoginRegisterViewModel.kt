package com.example.app_iliyan.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_iliyan.model.User
import com.example.app_iliyan.repository.ChatInterface
import com.example.app_iliyan.repository.ChatRepo

class LoginRegisterViewModel : ViewModel(), ChatInterface {
  override val chatRepo: ChatRepo = ChatRepo()
  val loginEvent = MutableLiveData<Boolean>()
  val registerEvent = MutableLiveData<Boolean>()

  suspend fun handleLoginClick(user: User, onResult: (String) -> Unit) {
    val isLoginSuccess = chatRepo.userRepo.handleModifyConfirmAuthUser("Login", user, onResult)

    if (isLoginSuccess) {
      loginEvent.value = true
    }
  }

  suspend fun handleRegisterClick(user: User, onResult: (String) -> Unit) {
    val isRegisterSuccess = chatRepo.userRepo.handleModifyConfirmAuthUser("SignUp", user, onResult)

    if (isRegisterSuccess) registerEvent.value = true
  }
}
