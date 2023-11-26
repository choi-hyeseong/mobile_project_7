package com.home.mindsnap.usecase

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.home.mindsnap.repository.image.PromptGenerator
import java.io.File

class ShareImage(private val context : Context) {

    fun shareImage(fileName : String) : Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, "com.home.mindsnap.fileprovider", File(context.filesDir, "${fileName}.jpeg")))
        }
    }
}