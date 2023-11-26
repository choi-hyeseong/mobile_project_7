package com.home.mindsnap.repository.gallery

import android.content.Intent
import android.graphics.Bitmap
import com.home.mindsnap.model.Image

interface GalleryRepository {
    //이미지를 가져오면 저장도 할줄 알아야 함..?
    fun isImageExists(fileName: String) : Boolean
    suspend fun saveImage(image: Bitmap, fileName : String)

    suspend fun getAllImages() : List<Image>

    fun shareImage(fileName: String) : Intent

}