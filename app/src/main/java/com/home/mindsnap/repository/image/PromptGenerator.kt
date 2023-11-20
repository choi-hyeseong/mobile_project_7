package com.home.mindsnap.repository.image

import com.home.mindsnap.type.ArtStyle

class PromptGenerator {

    fun generate(prompt: String, artStyle: ArtStyle) : String {
        return when(artStyle) {
            ArtStyle.NONE -> prompt
            else -> "$prompt with art style $artStyle"
        }
    }
}