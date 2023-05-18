package com.pradeep.moodtracker.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

actual fun getPlatformName(): String {
    return "Android"
}

actual suspend fun getImageBmp(url: String): ByteArray {
    return createWrappedHttpClient().getAsBytes(url)
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return toAndroidBitmap().asImageBitmap()
}

fun ByteArray.toAndroidBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
}

actual fun createWrappedHttpClient(): WrappedHttpClient {
    return object :WrappedHttpClient{
        override suspend fun getAsBytes(urlString: String): ByteArray {
            return HttpClient(OkHttp).get(urlString).readBytes()
        }
    }
}