package com.pradeep.moodtracker.common

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jetbrains.skia.Image

actual fun getPlatformName(): String {
    return "Desktop"
}

actual suspend fun getImageBmp(url: String): ByteArray {
    return createWrappedHttpClient().getAsBytes(url)
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

actual fun createWrappedHttpClient(): WrappedHttpClient {
    return object :WrappedHttpClient{
        override suspend fun getAsBytes(urlString: String): ByteArray {
            return HttpClient(CIO).get(urlString).readBytes()
        }

    }
}