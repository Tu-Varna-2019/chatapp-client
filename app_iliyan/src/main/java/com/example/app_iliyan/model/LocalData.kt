package com.example.app_iliyan.model

import android.app.Application

class LocalData : Application() {
  var authenticatedUser: User? = null

  override fun onCreate() {
    super.onCreate()
  }
}
