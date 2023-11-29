package com.home.mindsnap.repository.gallery.dao

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.content.FileProvider
import com.home.mindsnap.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
                readImage(file.absolutePath)?.let { image ->
                    result.add(
                        Image(
                            file.name, image))
                } //확장자 제거후 입력 -> 확장자 포함에서 입력
            }
        }
        return result
    }

    override suspend fun saveImage(image: Bitmap, fileName: String) {
        withContext(Dispatchers.IO) {
            val output = context.openFileOutput("$fileName.jpg", Context.MODE_PRIVATE)
            image.compress(Bitmap.CompressFormat.JPEG, 100, output)
            output.close()
        }
    }

    private fun readImage(filePath: String): Bitmap? {
        return BitmapFactory.decodeFile(filePath)
    }

    override fun shareImage(fileName: String): Intent {
        Log.w("asdf", context.fileList().contentToString())
        return Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    context,
                    "com.home.mindsnap.fileprovider",
                    File(context.filesDir, fileName)))
        }
    }

    override fun isImageExists(fileName: String): Boolean {
        return context.fileList().map { it.split(".")[0] }.find { it.equals(fileName, ignoreCase = true) } != null
    }
}