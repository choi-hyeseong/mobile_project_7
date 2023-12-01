package com.home.mindsnap.module

import android.content.Context
import android.content.SharedPreferences
import com.home.mindsnap.repository.gallery.dao.GalleryDao
import com.home.mindsnap.repository.gallery.dao.LocalGalleryDao
import com.home.mindsnap.repository.image.PromptGenerator
import com.home.mindsnap.repository.image.dao.ImageGenDao
import com.home.mindsnap.repository.image.dao.openai.OpenAIGenService
import com.home.mindsnap.repository.image.dao.openai.OpenAIImageGenDao
import com.home.mindsnap.repository.user.dao.PreferenceUserDao
import com.home.mindsnap.repository.user.dao.UserDao
import com.home.mindsnap.component.BitmapGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    private val PREFERENCE = "IMAGE_GEN"

    @Provides
    @Singleton
    fun provideImageGenDao(promptGenerator: PromptGenerator, openAIGenService: OpenAIGenService, bitmapGenerator: BitmapGenerator) : ImageGenDao {
        return OpenAIImageGenDao(promptGenerator, openAIGenService, bitmapGenerator)
    }

    @Provides
    @Singleton
    fun provideUserDao(preferences: SharedPreferences) : UserDao {
        return PreferenceUserDao(preferences)
    }

    @Provides
    @Singleton
    fun provideGalleryDao(@ApplicationContext context: Context) : GalleryDao {
        return LocalGalleryDao(context)
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
    }
}