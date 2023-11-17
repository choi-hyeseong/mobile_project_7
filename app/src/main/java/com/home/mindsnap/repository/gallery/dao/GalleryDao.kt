package com.home.mindsnap.repository.gallery.dao

import android.graphics.Bitmap
import com.home.mindsnap.model.Image

interface GalleryDao {

    suspend fun getAllImages() : List<Image>

    suspend fun saveImage(image : Bitmap, fileName: String)

}