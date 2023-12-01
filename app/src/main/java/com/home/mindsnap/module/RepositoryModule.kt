package com.home.mindsnap.module

import com.home.mindsnap.repository.gallery.GalleryRepository
import com.home.mindsnap.repository.gallery.LocalGalleryRepository
import com.home.mindsnap.repository.gallery.dao.GalleryDao
import com.home.mindsnap.repository.image.ImageGenRepository
import com.home.mindsnap.repository.image.OpenAIImageGenRepository
import com.home.mindsnap.repository.image.dao.ImageGenDao
import com.home.mindsnap.repository.user.PreferenceUserRepository
import com.home.mindsnap.repository.user.UserRepository
import com.home.mindsnap.repository.user.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideGalleryRepository(galleryDao: GalleryDao) : GalleryRepository {
        return LocalGalleryRepository(galleryDao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao : UserDao) : UserRepository {
        return PreferenceUserRepository(userDao)
    }

    @Provides
    @Singleton
    fun provideImageGenRepository(imageGenDao: ImageGenDao) : ImageGenRepository {
        return OpenAIImageGenRepository(imageGenDao)
    }
}