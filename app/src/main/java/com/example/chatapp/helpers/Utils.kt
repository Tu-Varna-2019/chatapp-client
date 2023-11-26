package com.example.chatapp.helpers

import android.content.Context
import android.util.Patterns
import android.widget.Toast

class Utils {

    fun base64(value: String): String {
        return java.util.Base64.getEncoder().encodeToString(value.toByteArray())
    }

     fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun showToast(context: Context, message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}