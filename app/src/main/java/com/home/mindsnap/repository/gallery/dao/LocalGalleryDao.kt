package com.home.mindsnap.repository.gallery.dao

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.FileProvider
import com.home.mindsnap.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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
        val name = if (fileName.contains(".")) fileName else fileName.plus(".jpg")
        return Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    context,
                    "com.home.mindsnap.fileprovider",
                    File(context.filesDir, name)))
        }
    }

    override fun isImageExists(fileName: String): Boolean {
        //filename도 동일하게 확장자 제거
        return context.fileList().map { it.split(".")[0] }.find { it.equals(fileName.split(".")[0], ignoreCase = true) } != null
    }
}