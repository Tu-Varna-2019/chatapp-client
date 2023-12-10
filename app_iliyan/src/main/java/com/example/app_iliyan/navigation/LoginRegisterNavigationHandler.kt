package com.example.app_iliyan.navigation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.app_iliyan.view.HomeActivity
import com.example.app_iliyan.view.LoginActivity
import com.example.app_iliyan.view_model.LoginRegisterViewModel

class LoginRegisterNavigationHandler {

  fun observeLoginEvent(
    lifecycleOwner: LifecycleOwner,
    loginRegisterViewModel: LoginRegisterViewModel,
    context: Context
  ) {
    loginRegisterViewModel.loginEvent.observe(
      lifecycleOwner,
      Observer { shouldLogin ->
        if (shouldLogin) {
          val intent = Intent(context, HomeActivity::class.java)
          context.startActivity(intent)
          loginRegisterViewModel.loginEvent.value = false
        }
      }
    )
  }

  fun observeRegisterEvent(
    lifecycleOwner: LifecycleOwner,
    loginRegisterViewModel: LoginRegisterViewModel,
    context: Context
  ) {
    loginRegisterViewModel.registerEvent.observe(
      lifecycleOwner,
      Observer { shouldRegister ->
        if (shouldRegister) {
          val intent = Intent(context, LoginActivity::class.java)
          context.startActivity(intent)
          loginRegisterViewModel.registerEvent.value = false
        }
      }
    )
  }
}
