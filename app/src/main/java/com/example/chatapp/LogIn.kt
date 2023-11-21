package com.example.chatapp
import android.os.AsyncTask
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class LogIn : AppCompatActivity(){

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(Intent(intent))

           /* GlobalScope.launch(Dispatchers.Main) {
                val result = performNetworkOperation()
                // Handle the result (e.g., update UI or display a message)
                println("Received from server: $result")
            }*/
        }
    }



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