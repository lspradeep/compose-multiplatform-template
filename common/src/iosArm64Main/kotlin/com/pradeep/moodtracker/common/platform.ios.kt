package com.pradeep.moodtracker.common

import androidx.compose.ui.graphics.ImageBitmap

actual fun getPlatformName(): String {
    TODO("Not yet implemented")
}

actual fun createWrappedHttpClient(): WrappedHttpClient {
    TODO("Not yet implemented")
}

actual suspend fun getImageBmp(url: String): ByteArray {
    TODO("Not yet implemented")
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    TODO("Not yet implemented")
}