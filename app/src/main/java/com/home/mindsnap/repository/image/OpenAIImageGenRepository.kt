package com.home.mindsnap.repository.image

import android.graphics.Bitmap
import com.home.mindsnap.repository.image.dao.ImageGenDao
import com.home.mindsnap.type.ArtStyle


class OpenAIImageGenRepository(private val genDao: ImageGenDao) :
    ImageGenRepository {
    //로컬에 저장할려면 application context 필요
    override suspend fun generateImage(prompt: String, style: ArtStyle): Bitmap {
        return genDao.getImage(prompt, style)
    }

}