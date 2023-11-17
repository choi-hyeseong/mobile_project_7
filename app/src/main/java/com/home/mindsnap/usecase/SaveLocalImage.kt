package com.home.mindsnap.usecase

import android.graphics.Bitmap
import com.home.mindsnap.repository.gallery.GalleryRepository

class SaveLocalImage(private val galleryRepository: GalleryRepository) {

    suspend fun saveImage(image : Bitmap, fileName : String) {
        galleryRepository.saveImage(image, fileName)
    }
}