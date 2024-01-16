package com.example.app_iliyan.helpers

import android.util.Patterns
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class Utils {
  companion object {
    val logger: Logger = LogManager.getLogger(Utils::class.java)
    const val SERVER_REQUEST_DELAY = 10000L

    fun isValidEmail(email: String): Boolean {
      return !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    // chars >= 8 chars, lowercase + uppercase +digits
    fun isValidPassword(password: String): Boolean {
      val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$".toRegex()
      return !password.matches(passwordRegex)
    }
  }
}
