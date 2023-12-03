package com.example.app_iliyan.view.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

object SnackbarManager {
  val snackbarHostState = SnackbarHostState()

  suspend fun showSnackbar(message: String) {
    snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun ScaffoldSnackbar(content: @Composable (PaddingValues) -> Unit) {
    Scaffold(snackbarHost = { SnackbarHost(SnackbarManager.snackbarHostState) }) { paddingValues ->
      content(paddingValues)
    }
  }
}
