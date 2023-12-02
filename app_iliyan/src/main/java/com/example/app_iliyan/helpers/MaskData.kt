package com.example.app_iliyan.helpers

class MaskData {
  companion object {
    fun base64Encode(value: String): String {
      return java.util.Base64.getEncoder().encodeToString(value.toByteArray())
    }
  }
}
