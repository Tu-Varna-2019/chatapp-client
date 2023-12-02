package com.example.app_iliyan.utility.network

import com.example.app_iliyan.helpers.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class SocketConnection constructor() {

  private val socket: Socket by lazy { Socket(SERVER_ADDRESS, PORT) }

  private val writer: BufferedWriter by lazy {
    BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
  }

  private val reader: BufferedReader by lazy {
    BufferedReader(InputStreamReader(socket.getInputStream()))
  }

  companion object SocketSingleton {
    private const val SERVER_ADDRESS = "10.0.2.2"
    private const val PORT = 8081

    @Volatile private var connection: SocketConnection? = null

    fun getConnection(): SocketConnection {
      return connection
          ?: synchronized(this) { connection ?: SocketConnection().also { connection = it } }
    }
  }

  suspend fun connectToServer(message: String): String =
      withContext(Dispatchers.IO) {
        try {
          val connectionInstance = getConnection()

          connectionInstance.writer.write(message)
          connectionInstance.writer.newLine()
          connectionInstance.writer.flush()

          val serverResponse = connectionInstance.reader.readLine()
          Utils.logger.info("Received from server: $serverResponse")

          connectionInstance.writer.close()
          connectionInstance.reader.close()
          connectionInstance.socket.close()

          serverResponse
        } catch (e: Exception) {
          e.printStackTrace()
          "Connection failed"
        }
      }
}
