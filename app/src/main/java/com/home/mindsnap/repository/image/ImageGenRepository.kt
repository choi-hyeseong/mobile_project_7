package com.home.mindsnap.repository.image

import android.graphics.Bitmap
import com.home.mindsnap.type.ArtStyle

interface ImageGenRepository {

    suspend fun generateImage(prompt : String, style : ArtStyle) : Bitmap
}