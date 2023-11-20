package com.home.mindsnap.module

import com.home.mindsnap.intercepter.OpenAIIInterceptor
import com.home.mindsnap.repository.image.dao.openai.OpenAIGenService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

class RestModule {

    private val URL: String = "https://api.openai.com/v1/"
    private val TOKEN: String = "" //TODO

    //header값 지정해주는 okhttp client (oauth 사용을 위해 bearer 헤더 추가)
    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(OpenAIIInterceptor(TOKEN)).build()
    }


    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder().baseUrl(URL).client(getOkHttpClient()).addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun getOpenAIService(retrofit: Retrofit) : OpenAIGenService {
        return retrofit.create(OpenAIGenService::class.java)
    }



}