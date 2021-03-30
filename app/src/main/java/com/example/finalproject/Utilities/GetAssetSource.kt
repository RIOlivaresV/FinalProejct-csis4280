package com.example.finalproject.Utilities

import android.content.Context
import android.util.Log
import java.io.InputStream
import java.nio.channels.AsynchronousFileChannel.open

class GetAssetSource {
    fun getAsset(context: Context): String? {
        var string: String = ""
        try {
            val inputStream: InputStream = context.assets.open("source.txt")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            string = String(buffer)
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }
        return string
    }
}