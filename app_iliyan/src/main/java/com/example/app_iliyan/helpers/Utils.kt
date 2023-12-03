package com.example.app_iliyan.helpers

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import org.apache.logging.log4j.LogManager

class Utils {
  companion object {
    val logger = LogManager.getLogger(Utils::class.java)

    fun showToast(context: Context, message: CharSequence) {
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

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
