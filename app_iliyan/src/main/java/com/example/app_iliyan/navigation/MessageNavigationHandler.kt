package com.example.app_iliyan.navigation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.app_iliyan.view.HomeActivity
import com.example.app_iliyan.view_model.MessageViewModel

class MessageNavigationHandler {

  fun observeBackToHomeEvent(
    lifecycleOwner: LifecycleOwner,
    messageViewModel: MessageViewModel,
    context: Context
  ) {
    messageViewModel.backHomeEvent.observe(
      lifecycleOwner,
      Observer { shouldBackToHome ->
        if (shouldBackToHome) {
          val intent = Intent(context, HomeActivity::class.java)
          context.startActivity(intent)
          messageViewModel.backHomeEvent.value = false
        }
      }
    )
  }
}
