package com.example.chatapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.io.encoding.Base64

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

    fun base64(value: String): String {
         return java.util.Base64.getEncoder().encodeToString(value.toByteArray())
    }

    fun hashPasswordWithSalt(password: String, salt: ByteArray): String {
        val bytes = password.toByteArray()
        val combined = ByteArray(bytes.size + salt.size)
        System.arraycopy(bytes, 0, combined, 0, bytes.size)
        System.arraycopy(salt, 0, combined, bytes.size, salt.size)

        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(combined)

        return digest.joinToString("") { "%02x".format(it) }
    }
    fun generateSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }
}