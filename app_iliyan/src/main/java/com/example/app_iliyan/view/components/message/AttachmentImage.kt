package com.example.app_iliyan.view.components.message

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.example.app_iliyan.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val bitmapCache = LruCache<String, Bitmap>(/* sizeInKiloBytes */ 1024 * 1024 * 10) // 10MB cache

@Composable
fun AttachmentImage(
  encodedAttachment: String,
  modifier: Modifier = Modifier,
  placeholderRes: Int = R.drawable.group
) {
  var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
  var loadKey by remember { mutableStateOf(0) }

  LaunchedEffect(key1 = encodedAttachment, key2 = loadKey) {
    val cachedBitmap = bitmapCache.get(encodedAttachment)
    if (cachedBitmap != null) {
      bitmapState = cachedBitmap
    } else {
      bitmapState =
        decodeBase64ToBitmapInBackground(encodedAttachment)?.also {
          bitmapCache.put(encodedAttachment, it)
        }
    }
  }

  DisposableEffect(Unit) { onDispose { loadKey++ } }

  val imageBitmap = bitmapState?.asImageBitmap() ?: ImageBitmap.imageResource(placeholderRes)

  Image(
    bitmap = imageBitmap,
    contentDescription = "",
    modifier = modifier.height(200.dp).fillMaxWidth(),
    contentScale = ContentScale.Crop
  )
}

suspend fun decodeBase64ToBitmapInBackground(base64String: String): Bitmap? =
  withContext(Dispatchers.IO) {
    try {
      val imageBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
      BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    } catch (e: Exception) {
      null
    }
  }
