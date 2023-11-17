package com.home.mindsnap.usecase

import android.graphics.Bitmap
import com.home.mindsnap.repository.image.ImageGenRepository
import com.home.mindsnap.type.ArtStyle

class GenerateImage(private val imageGenRepository: ImageGenRepository) {

    suspend fun generateImage(prompt : String, style: ArtStyle) : Bitmap {
        return imageGenRepository.generateImage(prompt, style)
    }
}