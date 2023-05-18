package com.pradeep.moodtracker.common

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource
import org.jetbrains.skia.Image
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.xhr.XMLHttpRequest
import org.w3c.xhr.XMLHttpRequestResponseType
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.wasm.unsafe.UnsafeWasmMemoryApi
import kotlin.wasm.unsafe.withScopedMemoryAllocator

actual suspend fun getImageBmp(url: String): ByteArray {
    return createWrappedHttpClient().getAsBytes(url)
}

actual fun createWrappedHttpClient(): WrappedHttpClient {
    return object : WrappedHttpClient {
        override suspend fun getAsBytes(urlString: String): ByteArray {
            return JSResourceImpl(urlString).readBytes()
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
private abstract class AbstractResourceImpl(val path: String) : Resource {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is AbstractResourceImpl) {
            path == other.path
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }
}

private class JSResourceImpl(path: String) : AbstractResourceImpl(path) {
    override suspend fun readBytes(): ByteArray {
        return suspendCoroutine { continuation ->
            val req = XMLHttpRequest()
            req.open("GET", path, true)
            req.responseType = "arraybuffer".asDynamic().unsafeCast<XMLHttpRequestResponseType>()

            req.onload = { _ ->
                val arrayBuffer = req.response
                if (arrayBuffer is ArrayBuffer) {
                    val size = arrayBuffer.byteLength
                    continuation.resume(arrayBuffer.toByteArray())
                } else {
                    continuation.resumeWithException(MissingResourceException(path))
                }
                null
            }
            req.send(null)
        }
    }
}

private class MissingResourceException constructor(path: String) :
    Exception("Missing resource with path: $path")


private fun ArrayBuffer.toByteArray(): ByteArray {
    val source = Int8Array(this, 0, byteLength)
    return jsInt8ArrayToKotlinByteArray(source)
}

@JsFun(
    """ (src, size, dstAddr) => {
        const mem8 = new Int8Array(wasmExports.memory.buffer, dstAddr, size);
        mem8.set(src);
    }
"""
)
internal external fun jsExportInt8ArrayToWasm(src: Int8Array, size: Int, dstAddr: Int)

internal fun jsInt8ArrayToKotlinByteArray(x: Int8Array): ByteArray {
    val size = x.length

    @OptIn(UnsafeWasmMemoryApi::class)
    return withScopedMemoryAllocator { allocator ->
        val memBuffer = allocator.allocate(size)
        val dstAddress = memBuffer.address.toInt()
        jsExportInt8ArrayToWasm(x, size, dstAddress)
        ByteArray(size) { i -> (memBuffer + i).loadByte() }
    }
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}