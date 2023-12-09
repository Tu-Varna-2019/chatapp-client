package com.example.app_iliyan.navigation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.app_iliyan.view.LoginActivity
import com.example.app_iliyan.view_model.HomeViewModel

class HomeNavigationHandler {
  fun observeLogoutEvent(
    lifecycleOwner: LifecycleOwner,
    homeViewModel: HomeViewModel,
    context: Context
  ) {
    homeViewModel.logoutEvent.observe(
      lifecycleOwner,
      Observer { shouldLogout ->
        if (shouldLogout) {
          val intent = Intent(context, LoginActivity::class.java)
          context.startActivity(intent)
          homeViewModel.logoutEvent.value = false
        }
      }
    )
  }
}
