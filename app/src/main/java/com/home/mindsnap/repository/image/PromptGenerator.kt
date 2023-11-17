package com.home.mindsnap.repository.image

import com.home.mindsnap.type.ArtStyle

class PromptGenerator {

    fun generate(prompt: String, artStyle: ArtStyle) : String {
        return "create art with prompt $prompt with art style $artStyle"
    }
}