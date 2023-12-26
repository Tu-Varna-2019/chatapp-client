package com.example.app_iliyan.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.util.Base64

class MaskData {
  companion object {
    fun base64Encode(value: String): String {
      return Base64.getEncoder().encodeToString(value.toByteArray())
    }

    fun base64EncodeUri(context: Context, uri: Uri): String {
      context.contentResolver.openInputStream(uri).use { inputStream ->
        val bytes = inputStream?.readBytes()
        return Base64.getEncoder().encodeToString(bytes)
      }
    }

    fun decodeBase64ToBitmap(base64String: String): Bitmap? {
      return try {
        val imageBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
      } catch (e: IllegalArgumentException) {
        null
      }
    }
  }
}
