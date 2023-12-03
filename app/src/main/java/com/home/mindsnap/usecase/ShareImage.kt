package com.home.mindsnap.usecase

import android.content.Intent
import com.home.mindsnap.repository.gallery.GalleryRepository

class ShareImage(private val galleryRepository: GalleryRepository) {

    fun shareImage(fileName : String) : Intent {
        return galleryRepository.shareImage(fileName)
    }
}