package com.home.mindsnap.repository.gallery

import android.content.Context
import android.graphics.Bitmap
import com.home.mindsnap.model.Image
import com.home.mindsnap.repository.gallery.dao.GalleryDao
import com.home.mindsnap.type.ArtStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalGalleryRepository(private val galleryDao: GalleryDao) : GalleryRepository {

    override suspend fun saveImage(image: Bitmap, fileName : String) {
        //로컬에 저장
        galleryDao.saveImage(image, fileName)
    }


    override suspend fun getAllImages(): List<Image> {
        return galleryDao.getAllImages()
    }
}