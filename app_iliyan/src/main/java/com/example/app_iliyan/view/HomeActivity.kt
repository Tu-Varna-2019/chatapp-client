package com.example.app_iliyan.view

import android.annotation.SuppressLint
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

class HomeActivity : ComponentActivity() {

  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val userObj = User()

    setContent {
      ScaffoldSnackbar {
        val context = LocalContext.current
        HomeLayout(
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
fun HomeLayout(user: User, onSignUpClick: () -> Unit, onBackClick: () -> Unit) {
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
@Composable
fun HomeLayoutPreview() {
  val dummyUser = User()
  var message by remember { mutableStateOf("") }
  HomeLayout(
      user = dummyUser,
      onSignUpClick = { message = "Goto Sign up!" },
      onBackClick = { message = "Goto Sign up!" })
  // Test if the message is not empty, then display it
  if (message.isNotEmpty()) {
    Text(text = message)
  }
}
