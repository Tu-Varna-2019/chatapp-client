package com.example.app_iliyan.model

import android.app.Application
import android.content.Context

class LocalData : Application() {
  var authenticatedUser: User? = null

  override fun onCreate() {
    super.onCreate()
    instance = this
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

    fun getAppContext(): Context {
      return instance!!.applicationContext
    }
  }
}
