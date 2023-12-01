package com.home.mindsnap.component

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream

class BitmapGenerator {

    fun decodeStream(stream : InputStream) : Bitmap {
        return BitmapFactory.decodeStream(stream)
    }

    fun decodeByte(array: ByteArray) : Bitmap {
        return BitmapFactory.decodeByteArray(array, 0, array.size)
    }
}