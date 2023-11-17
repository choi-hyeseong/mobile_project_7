package com.home.mindsnap.usecase

import com.home.mindsnap.model.Image
import com.home.mindsnap.repository.gallery.GalleryRepository

class GetAllImages(private val galleryRepository: GalleryRepository) {

    suspend fun getAllImages() : List<Image> {
        return galleryRepository.getAllImages()
    }
}