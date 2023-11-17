package com.home.mindsnap.repository.gallery

import com.home.mindsnap.model.Image

interface GalleryRepository {

    suspend fun getAllImages() : List<Image>
}