package com.example.app_iliyan.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_iliyan.model.User
import com.example.app_iliyan.view_model.UserViewModel

class LoginActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val userObj = User()

    setContent {
      UserLoginUpForm(
          user = userObj,
          onLoginClick = { UserViewModel.handleLoginUserClick(userObj) },
          onGotoSignUpClick = {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
          })
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLoginUpForm(user: User, onLoginClick: () -> Unit, onGotoSignUpClick: () -> Unit) {

  Column(modifier = Modifier.fillMaxWidth().padding(13.dp)) {
    TextField(
        value = user.email,
        onValueChange = { user.email = it },
        placeholder = { Text("Enter your email") },
        modifier = Modifier.fillMaxWidth())

    Spacer(modifier = Modifier.height(8.dp))

    TextField(
        value = user.password,
        onValueChange = { user.password = it },
        placeholder = { Text("Enter your password") },
        modifier = Modifier.fillMaxWidth())

    Spacer(modifier = Modifier.height(16.dp))

    Button(onClick = { onLoginClick() }, modifier = Modifier.fillMaxWidth()) { Text("Log in") }

    Button(onClick = { onGotoSignUpClick() }, modifier = Modifier.fillMaxWidth()) {
      Text("Sign up")
    }

    Spacer(modifier = Modifier.height(8.dp))
  }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun UserLoginUpFormPreview() {
  val dummyUser = User()
  var message by remember { mutableStateOf("") }

  UserLoginUpForm(
      user = dummyUser,
      onLoginClick = { UserViewModel.handleLoginUserClick(dummyUser) },
      onGotoSignUpClick = { message = "Goto Sign up!" })

  // Test if the message is not empty, then display it
  if (message.isNotEmpty()) {
    Text(text = message)
  }
}
