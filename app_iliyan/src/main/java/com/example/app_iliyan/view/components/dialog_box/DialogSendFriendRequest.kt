package com.example.app_iliyan.view.components.dialog_box

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.app_iliyan.helpers.Utils
import com.example.app_iliyan.model.LocalData

@Composable
fun DialogSendFriendRequest(onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
  val openDialog = remember { mutableStateOf(true) }
  val email = remember { mutableStateOf("") }
  val initialEmail = LocalData.getAuthenticatedUser()?.email ?: ""

  val isConfirmBtnDisabled =
    email.value.isEmpty() || email.value == initialEmail || Utils.isValidEmail(email.value)

  if (openDialog.value) {
    AlertDialog(
      onDismissRequest = { openDialog.value = false },
      title = { Text(text = "Add friend") },
      text = {
        Column {
          Text(text = "Enter your friend's email")
          TextField(
            value = email.value,
            isError = isConfirmBtnDisabled,
            onValueChange = { email.value = it },
            placeholder = { Text("Enter email") }
          )
          // Display error message
          if (isConfirmBtnDisabled) {
            // Display error message based on the type of error
            val errorMessage =
              if (email.value.isEmpty()) {
                "Email is empty"
              } else if (email.value == initialEmail) {
                "You cannot add yourself as a friend"
              } else {
                "Email is invalid"
              }
            Text(
              text = errorMessage,
              color = Color.Red,
              style = MaterialTheme.typography.bodyMedium
            )
          }
        }
      },
      confirmButton = {
        Button(
          enabled = !isConfirmBtnDisabled,
          onClick = {
            onConfirm(email.value)
            openDialog.value = false
          }
        ) {
          Text("Confirm")
        }
      },
      dismissButton = {
        Button(
          onClick = {
            onDismiss()
            openDialog.value = false
          }
        ) {
          Text("Cancel")
        }
      }
    )
  }
}
