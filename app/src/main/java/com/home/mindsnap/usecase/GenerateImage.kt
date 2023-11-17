package com.home.mindsnap.usecase

import android.graphics.Bitmap
import com.home.mindsnap.repository.image.ImageGenRepository
import com.home.mindsnap.type.ArtStyle

class GenerateImage(private val imageGenRepository: ImageGenRepository, private val saveLocalImage: SaveLocalImage) {

    suspend fun generateImage(prompt : String, style: ArtStyle) : Bitmap {
        val bitmap = imageGenRepository.generateImage(prompt, style)
        saveLocalImage.saveImage(bitmap, "$prompt with $style") //로컬에 저장
        return bitmap

    }
}