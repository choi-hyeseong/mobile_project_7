package com.home.mindsnap.module

import com.home.mindsnap.intercepter.OpenAIIInterceptor
import com.home.mindsnap.repository.image.dao.openai.OpenAIGenService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    private val URL: String = "https://api.openai.com/v1/"
    private val TOKEN: String = "" //TODO -> local로 옮길 예정

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder().baseUrl(URL).client(okHttpClient).addConverterFactory(
            GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient {
        //header값 지정해주는 okhttp client (oauth 사용을 위해 bearer 헤더 추가)
        return OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).addInterceptor(
            OpenAIIInterceptor(TOKEN)
        ).build()
    }

    @Provides
    @Singleton
    fun provideOpenAIService(retrofit: Retrofit) : OpenAIGenService {
        return retrofit.create(OpenAIGenService::class.java)
    }

}