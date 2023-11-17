package com.home.mindsnap.repository.gallery.dao

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.home.mindsnap.model.Image
import java.io.File
import java.util.ArrayList
import java.util.concurrent.ThreadLocalRandom

class LocalGalleryDao(private val context: Context) : GalleryDao {
    //내부 저장소 접근을 위한 ApplicationContext 접근
    override suspend fun getAllImages(): List<Image> {
        val dir = context.filesDir
        val result = ArrayList<Image>()
        dir.listFiles()?.let {
            for (file: File in it) {
                readImage(file.absolutePath)?.let { image -> result.add(Image(file.name, image)) }
            }
        }
        return result
    }

    private fun readImage(filePath: String): Bitmap? {
        return BitmapFactory.decodeFile(filePath)
    }
}