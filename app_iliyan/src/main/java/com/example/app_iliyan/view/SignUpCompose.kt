package com.example.app_iliyan.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
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
import com.example.app_iliyan.view.components.SnackbarManager.ScaffoldSnackbar
import com.example.app_iliyan.view_model.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : ComponentActivity() {

  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val userObj = User("", "", "")

    setContent {
      ScaffoldSnackbar {
        val context = LocalContext.current
        UserSignUpForm(
            user = userObj,
            onSignUpClick = {
              UserViewModel.handleSignUpUserClick(userObj, context) { resultMessage ->
                CoroutineScope(Dispatchers.Main).launch {
                  SnackbarManager.showSnackbar(resultMessage)
                }
              }
            },
            onBackClick = {
              val loginIntent = Intent(this, LoginActivity::class.java)
              startActivity(loginIntent)
            })
      }
    }
  }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSignUpForm(user: User, onSignUpClick: () -> Unit, onBackClick: () -> Unit) {
  val isUsernameError = user.username.isEmpty()
  val isEmailError = user.email.isEmpty() || Utils.isValidEmail(user.email)
  val isPasswordError = user.password.isEmpty() || Utils.isValidPassword(user.password)
  val isSubmitBtnDisabled = isUsernameError || isEmailError || isPasswordError

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(modifier = Modifier.fillMaxWidth().padding(13.dp)) {
      TextField(
          value = user.username,
          onValueChange = { user.username = it },
          placeholder = { Text("Enter your username") },
          isError = isUsernameError,
          modifier = Modifier.fillMaxWidth())
      // Display error message
      if (isUsernameError) {
        Text(
            text = "Username cannot be empty",
            color = Color.Red,
            style = MaterialTheme.typography.bodyMedium)
      }
      Spacer(modifier = Modifier.height(8.dp))

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
          onClick = { onSignUpClick() },
          enabled = !isSubmitBtnDisabled,
          modifier = Modifier.fillMaxWidth()) {
            Text("Sign Up")
          }

      Spacer(modifier = Modifier.height(8.dp))

      Button(onClick = { onBackClick() }, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
  }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun UserSignUpFormPreview() {
  val dummyUser = User("", "", "")
  var message by remember { mutableStateOf("") }
  UserSignUpForm(
      user = dummyUser,
      onSignUpClick = { message = "Goto Sign up!" },
      onBackClick = { message = "Goto Sign up!" })
  // Test if the message is not empty, then display it
  if (message.isNotEmpty()) {
    Text(text = message)
  }
}
