package com.example.app_iliyan.repository

import com.example.app_iliyan.BuildConfig
import com.example.app_iliyan.dataclass.ServerResponse
import com.example.app_iliyan.helpers.Utils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class SocketConnection {

  @OptIn(DelicateCoroutinesApi::class)
  companion object {
    private const val SERVER_ADDRESS = BuildConfig.SERVER_ADDRESS
    private const val PORT = BuildConfig.PORT

    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: BufferedWriter

    init {
      GlobalScope.launch(Dispatchers.IO) {
        setupConnection()
        listenForServerResponses()
      }
    }

    private suspend fun setupConnection() =
      withContext(Dispatchers.IO) {
        socket = Socket(SERVER_ADDRESS, PORT)
        reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
      }

    @OptIn(DelicateCoroutinesApi::class)
    private fun listenForServerResponses() {
      GlobalScope.launch(Dispatchers.IO) {
        try {
          while (isActive) {
            val serverResponse = reader.readLine()
            if (serverResponse != null) {
              Utils.logger.error("Received from server: $serverResponse")
              ServerDataHandler.parseResponse(serverResponse)
            }
          }
        } catch (e: Exception) {
          Utils.logger.error("Error: SocketConnection: {}", e.message)
        }
      }
    }

    suspend fun sendAndReceiveData(jsonString: String): ServerResponse =
      withContext(Dispatchers.IO) {
        setupConnection()
        try {
          Utils.logger.info("Sending data: $jsonString")

          writer.write(jsonString)
          writer.newLine()
          writer.flush()

          val serverResponse = reader.readLine()
          Utils.logger.error("Received from server: $serverResponse")

          ServerDataHandler.parseResponse(serverResponse)
        } catch (e: Exception) {
          Utils.logger.error("Error: SocketConnection: {}", e.message)
          ServerDataHandler.parseResponse(
            "{\"response\":{\"status\":\"Error\",\"message\":\"Socket connection error!\"}}"
          )
        }
      }
  }
}
