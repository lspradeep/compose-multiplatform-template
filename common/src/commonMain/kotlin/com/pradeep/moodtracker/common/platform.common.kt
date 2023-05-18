package com.pradeep.moodtracker.common

import androidx.compose.ui.graphics.ImageBitmap

expect fun getPlatformName(): String

expect fun createWrappedHttpClient(): WrappedHttpClient

expect suspend fun getImageBmp(url: String): ByteArray

expect fun ByteArray.toImageBitmap(): ImageBitmap

interface WrappedHttpClient {
    suspend fun getAsBytes(urlString: String): ByteArray
}