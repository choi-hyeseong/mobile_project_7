package com.home.mindsnap.repository.image.dao

import android.graphics.Bitmap
import com.home.mindsnap.repository.image.PromptGenerator
import com.home.mindsnap.type.ArtStyle

class OpenAIImageGenDao(private val generator : PromptGenerator) : ImageGenDao {
    override suspend fun getImage(prompt: String, style: ArtStyle): Bitmap {
        TODO("Not yet implemented")
    }
}