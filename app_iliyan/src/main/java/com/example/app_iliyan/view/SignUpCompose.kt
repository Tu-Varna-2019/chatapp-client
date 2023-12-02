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

class SignUpActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val userObj = User()

    setContent {
      UserSignUpForm(
          user = userObj,
          onSignUpClick = { UserViewModel.handleSignUpUserClick(userObj) },
          onBackClick = {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
          })
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSignUpForm(user: User, onSignUpClick: () -> Unit, onBackClick: () -> Unit) {

  Column(modifier = Modifier.fillMaxWidth().padding(13.dp)) {
    TextField(
        value = user.username,
        onValueChange = { user.username = it },
        placeholder = { Text("Enter your username") },
        modifier = Modifier.fillMaxWidth())

    Spacer(modifier = Modifier.height(8.dp))

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

    Button(onClick = { onSignUpClick() }, modifier = Modifier.fillMaxWidth()) { Text("Sign Up") }

    Spacer(modifier = Modifier.height(8.dp))

    Button(onClick = { onBackClick() }, modifier = Modifier.fillMaxWidth()) { Text("Back") }
  }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun UserSignUpFormPreview() {
  val dummyUser = User()
  var message by remember { mutableStateOf("") }
  UserSignUpForm(
      user = dummyUser,
      onSignUpClick = { UserViewModel.handleSignUpUserClick(dummyUser) },
      onBackClick = { message = "Goto Sign up!" })
  // Test if the message is not empty, then display it
  if (message.isNotEmpty()) {
    Text(text = message)
  }
}
