package com.example.app_iliyan.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun isChatLoadedIndicator(messageContent: String, isChatLoaded: Boolean, image: Int) {
  val showImage = remember { mutableStateOf(false) }
  LaunchedEffect(Unit) {
    delay(3000)
    if (isChatLoaded) {
      showImage.value = true
    }
  }
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    if (showImage.value) {

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(top = 50.dp)
      ) {
        Image(
          painter = painterResource(id = image),
          contentDescription = "Chat Icon",
          modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(text = messageContent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(206.dp))
      }
    } else {
      CircularProgressIndicator()
    }
  }
}
