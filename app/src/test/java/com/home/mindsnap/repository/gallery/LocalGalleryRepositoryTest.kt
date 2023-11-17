package com.home.mindsnap.repository.gallery

import android.graphics.Bitmap
import com.home.mindsnap.model.Image
import com.home.mindsnap.repository.gallery.dao.GalleryDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LocalGalleryRepositoryTest {
    lateinit var repository: GalleryRepository

    @Before
    fun init() {
        val dao = mockk<GalleryDao>()
        coEvery { dao.getAllImages() } returns listOf(mockk<Image>(), mockk<Image>())
        repository = LocalGalleryRepository(dao)
        //2개의 리스트 반환
    }

    @Test
    fun TEST_GET_ALL_IMAGE() {
        runBlocking {
            assertEquals(repository.getAllImages().size, 2)
        }
    }
}