package com.example.app_iliyan.model

import android.app.Application
import android.os.Handler
import android.os.Looper

class LocalData : Application() {
  var authenticatedUser: User? = null
  private val handler = Handler(Looper.getMainLooper())
  private val runnableCode: Runnable =
    object : Runnable {
      override fun run() {
        // NotificationService.showNotification(applicationContext, "hi", "hi", 0)
        handler.postDelayed(this, 5000)
      }
    }

  override fun onCreate() {
    super.onCreate()
    instance = this
    handler.post(runnableCode)
  }

  companion object {
    private var instance: LocalData? = null

    fun setAuthenticatedUser(
      username: String,
      email: String,
    ) {
      instance?.authenticatedUser = User(username, email, "")
    }

    fun getAuthenticatedUser(): User? {
      return instance?.authenticatedUser
    }
  }
}
