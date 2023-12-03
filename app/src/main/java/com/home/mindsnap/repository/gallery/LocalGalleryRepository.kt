package com.home.mindsnap.repository.gallery

import android.content.Intent
import android.graphics.Bitmap
import com.home.mindsnap.model.Image
import com.home.mindsnap.repository.gallery.dao.GalleryDao

class LocalGalleryRepository(private val galleryDao: GalleryDao) : GalleryRepository {

    override suspend fun saveImage(image: Bitmap, fileName : String) {
        //로컬에 저장
        galleryDao.saveImage(image, fileName)
    }


    override suspend fun getAllImages(): List<Image> {
        return galleryDao.getAllImages()
    }

    override fun shareImage(fileName: String): Intent {
        return galleryDao.shareImage(fileName)
    }

    override fun isImageExists(fileName: String): Boolean {
        return galleryDao.isImageExists(fileName)
    }
}