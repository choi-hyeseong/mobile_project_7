package com.home.mindsnap.repository.gallery.dao

import com.home.mindsnap.model.Image

interface GalleryDao {

    suspend fun getAllImages() : List<Image>

}