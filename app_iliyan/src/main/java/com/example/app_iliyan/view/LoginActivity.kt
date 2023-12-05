package com.example.app_iliyan.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.User
import com.example.app_iliyan.view.components.SnackbarManager
import com.example.app_iliyan.view_model.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val userObj = User("", "", "")

    setContent {
      SnackbarManager.ScaffoldSnackbar {
        val context = LocalContext.current
        val application = application
        UserLoginForm(
            user = userObj,
            onLoginClick = {
              UserViewModel.handleLoginUserClick(userObj, context) { resultMessage ->
                CoroutineScope(Dispatchers.Main).launch {
                  SnackbarManager.showSnackbar(resultMessage)
                }
              }
            },
            onGotoSignUpClick = {
              val signUpIntent = Intent(this, SignUpActivity::class.java)
              startActivity(signUpIntent)
            })
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLoginForm(user: User, onLoginClick: () -> Unit, onGotoSignUpClick: () -> Unit) {

  val isEmailError = user.email.isEmpty() || Utils.isValidEmail(user.email)
  val isPasswordError = user.password.isEmpty() || Utils.isValidPassword(user.password)
  val isLoginBtnDisabled = isEmailError || isPasswordError

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(modifier = Modifier.fillMaxWidth().padding(13.dp)) {
      TextField(
          value = user.email,
          onValueChange = { user.email = it },
          placeholder = { Text("Enter your email") },
          isError = isEmailError,
          modifier = Modifier.fillMaxWidth())

      // Display error message
      if (isEmailError) {
        Text(
            text = "Email is invalid",
            color = Color.Red,
            style = MaterialTheme.typography.bodyMedium)
      }

      Spacer(modifier = Modifier.height(8.dp))

      TextField(
          value = user.password,
          onValueChange = { user.password = it },
          placeholder = { Text("Enter your password") },
          visualTransformation = PasswordVisualTransformation(),
          isError = isPasswordError,
          modifier = Modifier.fillMaxWidth())

      // Display error message
      if (isPasswordError) {
        Text(
            text =
                "Password does not conform the rules: minimum 8 characters long, at least 1 lowercase, uppercase and digits ",
            color = Color.Red,
            style = MaterialTheme.typography.bodyMedium)
      }

      Spacer(modifier = Modifier.height(16.dp))

      Button(
          onClick = { onLoginClick() },
          enabled = !isLoginBtnDisabled,
          modifier = Modifier.fillMaxWidth()) {
            Text("Log in")
          }

      Spacer(modifier = Modifier.height(8.dp))

      Button(onClick = { onGotoSignUpClick() }, modifier = Modifier.fillMaxWidth()) {
        Text("Sign up")
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun UserLoginFormPreview() {
  val dummyUser = User("", "", "")
  var message by remember { mutableStateOf("") }
  UserLoginForm(
      user = dummyUser,
      onLoginClick = { message = "Goto Sign up!" },
      onGotoSignUpClick = { message = "Goto Sign up!" })
  // Test if the message is not empty, then display it
  if (message.isNotEmpty()) {
    Text(text = message)
  }
}
