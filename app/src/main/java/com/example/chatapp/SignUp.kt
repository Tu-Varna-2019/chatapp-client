package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity(){

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button //deklarira private promenliva s ime btnSignUp, lateinit - shte se izpolzva po kysno, var - stoinosta moje da byde promenena
    private lateinit var btnBack: Button
    private lateinit var helperTextView: TextView
    private lateinit var helperTextView1: TextView
    private lateinit var helperTextView2: TextView


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
        helperTextView = findViewById(R.id.helperTextView)
        helperTextView.setTextColor(ContextCompat.getColor(this, R.color.white)) // import color
        helperTextView1 = findViewById(R.id.helperTextView1)
        helperTextView1.setTextColor(ContextCompat.getColor(this, R.color.white))
        helperTextView2 = findViewById(R.id.helperTextView2)
        helperTextView2.setTextColor(ContextCompat.getColor(this, R.color.white))

        setValidationForEditText(edtName, helperTextView, btnSignUp)
        setValidationForEditText(edtEmail, helperTextView1, btnSignUp)
        setValidationForEditText(edtPassword, helperTextView2, btnSignUp)


        btnSignUp.setOnClickListener {
            val userName = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (userName.isBlank() || email.isBlank() || password.isBlank()) {
                showToast("Fill the empty field")
            } else {
                if(isValidEmail(email)){
                   GlobalScope.launch(Dispatchers.Main) {//work with database - reading/writing to files / network calls
                     val result = performSignUp(userName, email, password)
                      println("Received from server: $result")

                      if (result == "User has registered") {
                          val intent = Intent(this@SignUp, LogIn::class.java)
                          startActivity(intent)
                          showToast("You have registered")
                      } else {
                          showToast("Registration process failed")
                      }
                   }
                } else {
                    showToast("Incorrect email")
                }
            }
        }

        btnBack.setOnClickListener {
            var intent = Intent(this, LogIn::class.java)
            startActivity(Intent(intent))

        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    suspend fun performSignUp(userName: String, email: String, password: String): String {
        val connection = Connection()
        return connection.connectionMethod("$userName;$email;$password")
    }


    private fun setValidationForEditText(
        editText: EditText,
        helperTextView: TextView,
        signUpButton: Button
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = editText.text.toString()

                if (text.isBlank()) {
                    signUpButton.isEnabled = false
                } else {
                    if (s?.contains(";") == true) {
                        helperTextView.text = "You can't use symbol - ;"
                        helperTextView.setTextColor(Color.RED)
                        signUpButton.isEnabled = false
                    } else {
                        helperTextView.text = ""
                        signUpButton.isEnabled = true
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}