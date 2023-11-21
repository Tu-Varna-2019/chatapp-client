package com.example.chatapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

 class Connection {

     suspend fun connectionMethod(message: String): String =
             withContext(Dispatchers.IO) {
        val serverAddress = "10.0.2.2"
        val serverPort = 8081

        try {
            val socket = Socket(serverAddress, serverPort)
            val writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

            writer.write(message)
            writer.newLine()
            writer.flush()


            val serverResponse = reader.readLine()
            println("Received from server: $serverResponse")

            // Close the resources
            writer.close()
            reader.close()
            socket.close()

            serverResponse
        } catch (e: Exception) {
            e.printStackTrace()
            "Connection failed"
        }
    }


}