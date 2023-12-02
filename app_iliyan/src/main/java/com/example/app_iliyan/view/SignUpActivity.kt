package com.example.app_iliyan.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.app_iliyan.R
import com.example.app_iliyan.dataclass.UserData
import com.example.app_iliyan.dataclass.UserSignUpData
import com.example.app_iliyan.helpers.MaskData
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.utility.network.SocketConnection
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SignUpActivity : AppCompatActivity() {

  private lateinit var edtName: EditText
  private lateinit var edtEmail: EditText
  private lateinit var edtPassword: EditText
  private lateinit var btnSignUp:
      Button // deklarira private promenliva s ime btnSignUp, lateinit - shte se izpolzva po kysno,
             // var - stoinosta moje da byde promenena
  private lateinit var btnBack: Button

  @SuppressLint("MissingInflatedId")
  @OptIn(DelicateCoroutinesApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_up)

    edtName = findViewById(R.id.edt_name)
    edtEmail = findViewById(R.id.sign_up_edt_email)
    edtPassword = findViewById(R.id.sign_up_edt_password)
    btnSignUp = findViewById(R.id.sign_up_btn_sign_up)
    btnBack = findViewById(R.id.btnBack)

    btnSignUp.setOnClickListener {
      val userName = edtName.text.toString()
      val email = edtEmail.text.toString()
      val password = edtPassword.text.toString()

      val base64UserName = MaskData.base64Encode(userName)
      val base64Email = MaskData.base64Encode(email)
      val base64Password = MaskData.base64Encode(password)

      if (userName.isBlank() || email.isBlank() || password.isBlank()) {
        Utils.showToast(this, "Fill the empty field")
      } else {
        if (Utils.isValidEmail(email)) {
          GlobalScope.launch(
              Dispatchers.Main) { // work with database - reading/writing to files / network calls
                val result = performSignUp(base64UserName, base64Email, base64Password)
                println("Received from server: $result")

                if (result == "User has registered") {
                  val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                  startActivity(intent)
                  Utils.showToast(this@SignUpActivity, "You have registered")
                } else {
                  Utils.showToast(this@SignUpActivity, "Registration process failed")
                }
              }
        } else {
          Utils.showToast(this, "Incorrect email")
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

      val eventType: String = "SignUp"
      val encodedEventType = MaskData.base64Encode(eventType)
      val connection = SocketConnection()
      SocketConnection.getConnection()

      val userData = UserData(userName, email, password)

      // Create an instance of UserSignUpData
      val userSignUpData = UserSignUpData(encodedEventType, userData)

      val jsonString = Json.encodeToString(userSignUpData)
      println("json string - $jsonString")

      return connection.connectToServer(jsonString)
    } catch (e: Exception) {
      Log.e("SignUpError", e.message.toString())
      return "Connection failed: ${e.message}"
    }
  }
}
