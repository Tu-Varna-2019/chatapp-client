package com.example.app_iliyan.repository

import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.helpers.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class SocketConnection {

  companion object {
    private const val SERVER_ADDRESS = "10.0.2.2"
    private const val PORT = 8081

    @Volatile private var instance: SocketConnection? = null

    fun getInstance(): SocketConnection {
      return instance
        ?: synchronized(this) { instance ?: SocketConnection().also { instance = it } }
    }

    suspend fun sendAndReceiveData(jsonString: String): ServerResponse =
      withContext(Dispatchers.IO) {
        try {
          Socket(SERVER_ADDRESS, PORT).use { socket ->
            BufferedWriter(OutputStreamWriter(socket.getOutputStream())).use { writer ->
              BufferedReader(InputStreamReader(socket.getInputStream())).use { reader ->
                Utils.logger.info("Sending data: $jsonString")

                writer.write(jsonString)
                writer.newLine()
                writer.flush()

                val serverResponse = reader.readLine()
                Utils.logger.info("Received from server: $serverResponse")

                ServerDataHandler.parseResponse(serverResponse)
              }
            }
          }
        } catch (e: Exception) {
          Utils.logger.error("Error: SocketConnection: {}", e.printStackTrace())
          ServerDataHandler.parseResponse(
            "{\"response\":{\"status\":\"Error\",\"message\":\"Socket connection error!\"}}"
          )
        }
      }
  }
}
