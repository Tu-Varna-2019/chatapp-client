package com.example.chatapp
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class LoginActivity : AppCompatActivity(){

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(Intent(intent))

        }
    }


    // TODO: Implement the login functionality
    private suspend fun performNetworkOperation(): String? = withContext(Dispatchers.IO) {
        val serverAddress = "10.0.2.2"
        val serverPort = 8081

        return@withContext try {
            val socket = Socket(serverAddress, serverPort)
            val writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

            val msg: MutableList<String> = mutableListOf("a", "b", "c")
            for (message in msg) {
                writer.write(message)
                writer.newLine()
            }
            writer.flush()

            // Receive and return the server's response
            val serverResponse = reader.readLine()

            // Close the resources
            writer.close()
            reader.close()
            socket.close()

            serverResponse
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}