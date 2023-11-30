package com.example.app_iliyan

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class SocketConnection constructor() {

    val socket: Socket by lazy {
        Socket(SERVER_ADDRESS, PORT)
    }

    val writer: BufferedWriter by lazy {
        BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
    }

    val reader: BufferedReader by lazy {
        BufferedReader(InputStreamReader(socket.getInputStream()))
    }

    companion object SocketSingleton {
        private const val SERVER_ADDRESS = "10.0.2.2"
        private const val PORT = 8081

        @Volatile
        private var instance: SocketConnection? = null

        fun getInstance(): SocketConnection {
            return instance ?: synchronized(this) {
                instance ?: SocketConnection().also { instance = it }
            }
        }
    }

    suspend fun connectToServer(message: String): String =
        withContext(Dispatchers.IO) {

            try {
                // Retrieve the instance
                val connectionInstance = getInstance()

                // Use the instance to send the message
                connectionInstance.writer.write(message)
                connectionInstance.writer.newLine()
                connectionInstance.writer.flush()

                val serverResponse = connectionInstance.reader.readLine()
                println("Received from server: $serverResponse")

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