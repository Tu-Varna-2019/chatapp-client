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

class SocketConnection constructor() {

  private val SERVER_ADDRESS = "10.0.2.2"
  private val PORT = 8081

  private val socket: Socket by lazy { Socket(SERVER_ADDRESS, PORT) }

  private val writer: BufferedWriter by lazy {
    BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
  }

  private val reader: BufferedReader by lazy {
    BufferedReader(InputStreamReader(socket.getInputStream()))
  }

  companion object SocketSingleton {

    @Volatile private var connection: SocketConnection? = null

    private fun getConnection(): SocketConnection {
      return connection
          ?: synchronized(this) { connection ?: SocketConnection().also { connection = it } }
    }

    suspend fun sendAndReceiveData(jsonString: String): ServerResponse =
        withContext(Dispatchers.IO) {
          try {
            val connectionInstance = getConnection()
            Utils.logger.info("Sending data: $jsonString")

            connectionInstance.writer.write(jsonString)
            connectionInstance.writer.newLine()
            connectionInstance.writer.flush()

            val serverResponse = connectionInstance.reader.readLine()
            Utils.logger.info("Received from server: $serverResponse")

            connectionInstance.writer.close()
            connectionInstance.reader.close()
            connectionInstance.socket.close()

            ServerDataHandler.parseResponse(serverResponse)
          } catch (e: Exception) {
            e.printStackTrace()
            ServerDataHandler.parseResponse(
                "{\"response\":{\"status\":\"Error\",\"message\":\"Socket connection error!\"}}")
          }
        }
  }
}
