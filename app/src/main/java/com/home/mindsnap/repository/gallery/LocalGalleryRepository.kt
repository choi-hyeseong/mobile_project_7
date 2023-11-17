package com.home.mindsnap.repository.gallery

import com.home.mindsnap.model.Image
import com.home.mindsnap.repository.gallery.dao.GalleryDao

class LocalGalleryRepository(private val galleryDao: GalleryDao) : GalleryRepository {
    override suspend fun getAllImages(): List<Image> {
        return galleryDao.getAllImages()
    }
}