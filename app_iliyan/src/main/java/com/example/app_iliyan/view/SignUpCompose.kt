package com.example.app_iliyan.view

import android.content.res.Configuration
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSignUpForm(onSignUpClick: () -> Unit, onBackClick: () -> Unit) {
  var username by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  Column(modifier = Modifier.fillMaxWidth().padding(13.dp)) {
    TextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Username") },
        modifier = Modifier.fillMaxWidth())

    Spacer(modifier = Modifier.height(8.dp))

    TextField(
        value = email,
        onValueChange = { email = it },
        // label = { Text("Email") },
        placeholder = { Text("Enter something") },
        modifier = Modifier.fillMaxWidth())

    Spacer(modifier = Modifier.height(8.dp))

    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
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
  UserSignUpForm(
      onSignUpClick = {
        // Define what happens when the "Sign Up" button is clicked.
      },
      onBackClick = {
        // Define what happens when the "Back" button is clicked.
      })
}
