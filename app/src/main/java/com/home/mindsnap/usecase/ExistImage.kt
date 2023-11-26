package com.home.mindsnap.usecase

import com.home.mindsnap.repository.gallery.GalleryRepository

class ExistImage(private val galleryRepository: GalleryRepository) {

    fun existImage(fileName : String) : Boolean {
        return galleryRepository.isImageExists(fileName)
    }
}