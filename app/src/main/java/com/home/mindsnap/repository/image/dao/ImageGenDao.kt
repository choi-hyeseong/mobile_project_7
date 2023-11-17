package com.home.mindsnap.repository.image.dao

import android.graphics.Bitmap
import com.home.mindsnap.type.ArtStyle

interface ImageGenDao {

    suspend fun getImage(prompt: String, style: ArtStyle) : Bitmap
}