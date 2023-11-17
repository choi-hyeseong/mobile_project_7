package com.home.mindsnap

import com.home.mindsnap.repository.image.PromptGenerator
import com.home.mindsnap.type.ArtStyle
import org.junit.Assert.assertEquals
import org.junit.Test

class PromptGeneratorTest {

    private val promptGenerator = PromptGenerator()
    @Test
    fun TEST_GENERATOR() {
        val prompt : String = "cute doggy"
        val artStyle : ArtStyle = ArtStyle.THIRD
        assertEquals(promptGenerator.generate(prompt, artStyle), "create art with prompt $prompt with art style $artStyle")
    }

    @Test
    fun TEST_ENUM_FACTORY() {
        assertEquals(ArtStyle.fromString("3D"), ArtStyle.THIRD)
    }
}