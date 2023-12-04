package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.ServerResponse
import kotlinx.serialization.json.Json

class ServerDataHandler {
  companion object {
    fun parseResponse(jsonString: String): ServerResponse {
      val json = Json { ignoreUnknownKeys = true }
      return json.decodeFromString(ServerResponse.serializer(), jsonString)
    }
  }
}
