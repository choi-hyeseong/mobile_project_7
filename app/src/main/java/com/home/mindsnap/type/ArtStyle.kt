package com.home.mindsnap.type

import java.lang.Exception

enum class ArtStyle {
    NONE, THIRD, FANTASY;


    fun fromString(style: String): ArtStyle {
        return when (style) {
            "3D" -> THIRD
            "fantasy" -> FANTASY
            else -> try {
                ArtStyle.valueOf(style)
            }
            catch (e: IllegalArgumentException) {
                NONE
            }
        }
    }
}