package com.home.mindsnap.type

enum class ArtStyle(private val value: String) {
    NONE(""), THIRD("3D"), FANTASY("fantasy"), ARTISTIC("artistic"), FAIRYTALE("fairytale");

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromString(style: String): ArtStyle {
            return values().find { it.value.equals(style, true) } ?: try {
                    ArtStyle.valueOf(style)
                }
                catch (e: IllegalArgumentException) {
                    NONE
                }
        }
    }
}