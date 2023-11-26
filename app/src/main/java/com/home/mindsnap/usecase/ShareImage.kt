package com.home.mindsnap.usecase

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.home.mindsnap.repository.gallery.GalleryRepository
import com.home.mindsnap.repository.image.PromptGenerator
import java.io.File

class ShareImage(private val galleryRepository: GalleryRepository) {

    fun shareImage(fileName : String) : Intent {
        return galleryRepository.shareImage(fileName)
    }
}