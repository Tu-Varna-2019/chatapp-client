package com.example.app_iliyan.navigation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.app_iliyan.dataclass.GroupChatData
import com.example.app_iliyan.repository.ServerDataHandler
import com.example.app_iliyan.view.LoginActivity
import com.example.app_iliyan.view.MessageActivity
import com.example.app_iliyan.view_model.HomeViewModel
import kotlinx.serialization.json.Json

class HomeNavigationHandler {
  fun observeLogoutEvent(
    lifecycleOwner: LifecycleOwner,
    homeViewModel: HomeViewModel,
    context: Context,
  ) {
    homeViewModel.logoutEvent.observe(
      lifecycleOwner,
      Observer { shouldLogout ->
        if (shouldLogout) {
          val intent = Intent(context, LoginActivity::class.java)
          context.startActivity(intent)
          homeViewModel.logoutEvent.value = false
        }
      },
    )
  }

  fun observeGotoMessageEvent(
    lifecycleOwner: LifecycleOwner,
    homeViewModel: HomeViewModel,
    context: Context,
  ) {
    homeViewModel.gotoMessageEvent.observe(
      lifecycleOwner,
      Observer { groupChat ->
        val serializableGroupChat =
          groupChat?.let {
            GroupChatData(
              it.id,
              it.name ?: "",
              ServerDataHandler.convertListUserToListUserData(it.users),
            )
          }

        serializableGroupChat?.let {
          val json = Json.encodeToString(GroupChatData.serializer(), it)
          val intent =
            Intent(context, MessageActivity::class.java).apply { putExtra("groupChat", json) }
          context.startActivity(intent)
        }
      },
    )
  }
}
