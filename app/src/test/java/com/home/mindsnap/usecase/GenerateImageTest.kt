package com.home.mindsnap.usecase

import com.home.mindsnap.repository.image.ImageGenRepository
import com.home.mindsnap.type.ArtStyle
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GenerateImageTest {

    lateinit var generateImage: GenerateImage
    lateinit var saveLocalImage : SaveLocalImage
    @Before
    fun init() {
        val imageGen = mockk<ImageGenRepository>()
        saveLocalImage = mockk()
        generateImage = GenerateImage(imageGen, saveLocalImage)
        coEvery { imageGen.generateImage(any(), any()) } returns mockk()
        coEvery { saveLocalImage.saveImage(any(), any()) } returns Unit


    }

    @Test
    fun TEST_SAVE_IMAGE_CALLED() {
        val input = "happy cat"
        val artStyle = ArtStyle.FANTASY

        runBlocking {
            generateImage.generateImage(input, artStyle)
            coVerify(atLeast = 1) { saveLocalImage.saveImage(any(), any()) } //뭐했길래 통과되지..?
        }
    }
}