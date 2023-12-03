package com.home.mindsnap.module

import com.home.mindsnap.component.BitmapGenerator
import com.home.mindsnap.repository.image.PromptGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ComponentModule {

    @Provides
    @Singleton
    fun provideBitmapGenerator() : BitmapGenerator {
        return BitmapGenerator()
    }

    @Provides
    @Singleton
    fun providePromptGenerator() : PromptGenerator {
        return PromptGenerator()
    }
}