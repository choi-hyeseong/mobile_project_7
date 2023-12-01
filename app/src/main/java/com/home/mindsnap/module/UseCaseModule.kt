package com.home.mindsnap.module

import com.home.mindsnap.repository.gallery.GalleryRepository
import com.home.mindsnap.repository.image.ImageGenRepository
import com.home.mindsnap.repository.user.UserRepository
import com.home.mindsnap.usecase.ExistImage
import com.home.mindsnap.usecase.GenerateImage
import com.home.mindsnap.usecase.GetAllImages
import com.home.mindsnap.usecase.GetUserFirstJoined
import com.home.mindsnap.usecase.SaveLocalImage
import com.home.mindsnap.usecase.SaveUserVisited
import com.home.mindsnap.usecase.ShareImage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    //만약 리팩토링 한다면 각 레포지토리 - dao별로 묶어서 모듈 구성하면 좋을듯

    @Provides
    @Singleton
    fun provideExistImage(galleryRepository: GalleryRepository) : ExistImage {
        return ExistImage(galleryRepository)
    }

    @Provides
    @Singleton
    fun provideGenerateImage(imageGenRepository : ImageGenRepository) : GenerateImage {
        return GenerateImage(imageGenRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllImages(galleryRepository: GalleryRepository) : GetAllImages {
        return GetAllImages(galleryRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserFirstJoined(userRepository : UserRepository) : GetUserFirstJoined {
        return GetUserFirstJoined(userRepository)
    }

    @Provides
    @Singleton
    fun provideSaveLocalImage(galleryRepository: GalleryRepository) : SaveLocalImage {
        return SaveLocalImage(galleryRepository)
    }
    @Provides
    @Singleton
    fun provideSaveUserVisited(userRepository : UserRepository) : SaveUserVisited {
        return SaveUserVisited(userRepository)
    }

    @Provides
    @Singleton
    fun provideShareImage(galleryRepository: GalleryRepository) : ShareImage {
        return ShareImage(galleryRepository)
    }

}