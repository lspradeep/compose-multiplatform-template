package com.pradeep.moodtracker.common

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jetbrains.skia.Image

actual suspend fun getImageBmp(url: String): ByteArray {
    return createWrappedHttpClient().getAsBytes(url)
}

actual fun createWrappedHttpClient(): WrappedHttpClient {
    return object : WrappedHttpClient {
        private val ktorClient = HttpClient(JsClient())
        override suspend fun getAsBytes(urlString: String): ByteArray {
            return ktorClient.get(urlString).readBytes()
        }
    }
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}