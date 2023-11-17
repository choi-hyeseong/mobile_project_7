package com.home.mindsnap.repository.gallery.dao

import com.home.mindsnap.model.Image

class LocalGalleryDao : GalleryDao {
    override suspend fun getAllImages(): List<Image> {
        TODO("Not yet implemented")
    }
}