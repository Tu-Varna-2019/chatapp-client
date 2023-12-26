package com.example.app_iliyan.view.components.message

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.app_iliyan.helpers.MaskData

@Composable
fun AttachmentImage(encodedAttachment: String, modifier: Modifier = Modifier) {
  val bitmap = MaskData.decodeBase64ToBitmap(encodedAttachment)

  bitmap?.let {
    Image(
      bitmap = it.asImageBitmap(),
      contentDescription = "",
      modifier = modifier.height(200.dp).fillMaxWidth(),
      contentScale = ContentScale.Crop
    )
  }
}
