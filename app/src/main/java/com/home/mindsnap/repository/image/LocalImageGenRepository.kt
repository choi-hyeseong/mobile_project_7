package com.home.mindsnap.repository.image

import android.graphics.Bitmap
import com.home.mindsnap.repository.image.dao.ImageGenDao
import com.home.mindsnap.type.ArtStyle


class LocalImageGenRepository(private val genDao: ImageGenDao) : ImageGenRepository {

    override suspend fun generateImage(prompt: String, style: ArtStyle): Bitmap {
        val result = genDao.getImage(prompt, style)
        saveFile(result, prompt, style)
        return result
    }

    private suspend fun saveFile(image : Bitmap, prompt: String, style: ArtStyle) {
        //로컬에 저장
    }

}