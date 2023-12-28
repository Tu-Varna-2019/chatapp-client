package com.example.app_iliyan.view.components.dialog_box

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun DialogAddGroupChat(onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
  val openDialog = remember { mutableStateOf(true) }
  val groupchatName = remember { mutableStateOf("") }

  val isConfirmBtnDisabled = groupchatName.value.isEmpty()

  if (openDialog.value) {
    AlertDialog(
      onDismissRequest = { openDialog.value = false },
      title = { Text(text = "Create new group chat") },
      text = {
        Column {
          Text(text = "Enter group chat name")
          TextField(
            value = groupchatName.value,
            isError = isConfirmBtnDisabled,
            onValueChange = { groupchatName.value = it },
            placeholder = { Text("Name cannot be empty") }
          )
        }
      },
      confirmButton = {
        Button(
          enabled = !isConfirmBtnDisabled,
          onClick = {
            onConfirm(groupchatName.value)
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
