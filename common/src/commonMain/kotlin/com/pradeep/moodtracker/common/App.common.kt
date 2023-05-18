package com.pradeep.moodtracker.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.delay

@Composable
fun App() {
    var img by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(Unit) {
        try {
            img =
                getImageBmp("https://4kwallpapers.com/images/walls/thumbs_3t/7749.jpg").toImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    Text("Helloo ${getPlatformName()}")

//    println("img is $img")

  Surface {
      Box(modifier = Modifier.fillMaxSize().background(color = Color(0xFF8B0408)), contentAlignment = Alignment.Center) {
          if (img != null) {
              Image(
                  bitmap = img!!,
                  contentDescription = null,
                  modifier = Modifier.fillMaxWidth(),
                  contentScale = ContentScale.Crop
              )
          }
      }
  }
}

