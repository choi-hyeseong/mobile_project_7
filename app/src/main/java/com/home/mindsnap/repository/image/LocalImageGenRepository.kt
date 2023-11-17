package com.home.mindsnap.repository.image

import android.content.Context
import android.graphics.Bitmap
import com.home.mindsnap.repository.image.dao.ImageGenDao
import com.home.mindsnap.type.ArtStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LocalImageGenRepository(private val genDao: ImageGenDao, private val context: Context) :
    ImageGenRepository {
    //로컬에 저장할려면 application context 필요
    override suspend fun generateImage(prompt: String, style: ArtStyle): Bitmap {
        val result = genDao.getImage(prompt, style)
        saveFile(result, prompt, style)
        return result
    }

    private suspend fun saveFile(image: Bitmap, prompt: String, style: ArtStyle) {
        //로컬에 저장
        withContext(Dispatchers.IO) {
            val output = context.openFileOutput("$prompt with $style", Context.MODE_PRIVATE)
            image.compress(Bitmap.CompressFormat.JPEG, 100, output)
            output.close()
        }
    }

}