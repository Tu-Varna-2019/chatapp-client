package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.chatapp.helpers.Utils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SignUpActivity : AppCompatActivity(){

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button //deklarira private promenliva s ime btnSignUp, lateinit - shte se izpolzva po kysno, var - stoinosta moje da byde promenena
    private lateinit var btnBack: Button

    @SuppressLint("MissingInflatedId")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnBack = findViewById(R.id.btnBack)

        btnSignUp.setOnClickListener {
            val userName = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            val utils = Utils()
            val base64UserName = utils.base64(userName)
            val base64Email = utils.base64(email)
            val base64Password = utils.base64(password)

            if (userName.isBlank() || email.isBlank() || password.isBlank()) {
                utils.showToast(this,"Fill the empty field")
            } else {
                if(utils.isValidEmail(email)){
                   GlobalScope.launch(Dispatchers.Main) {//work with database - reading/writing to files / network calls
                     val result = performSignUp(base64UserName, base64Email, base64Password)
                      println("Received from server: $result")


                      if (result == "User has registered") {
                          val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                          startActivity(intent)
                          utils.showToast(this@SignUpActivity,"You have registered")
                      } else {
                          utils.showToast(this@SignUpActivity,"Registration process failed")
                      }
                   }
                } else {
                    utils.showToast(this,"Incorrect email")
                }
            }
        }

        btnBack.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(Intent(intent))

        }
    }

    suspend fun performSignUp(userName: String, email: String, password: String): String {
        try {
            val utils = Utils()
            val eventType: String = "SignUp"
            val encodedEventType = utils.base64(eventType)
            val connection = SocketConnection()
            SocketConnection.getInstance()

            val userMap = mapOf(
                "encodedEventType" to encodedEventType,
                "encodedUserName" to userName,
                "encodedEmail" to email,
                "encodedPassword" to password
            )
            val jsonString = Json.encodeToString(userMap)
            println("user map - $userMap")
            println("json string - $jsonString")

            return connection.connectToServer(jsonString)

        } catch (e: Exception) {
            e.printStackTrace()
            return "Connection failed: ${e.message}"
        }
    }

}