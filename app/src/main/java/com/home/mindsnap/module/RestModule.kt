package com.home.mindsnap.module

import com.home.mindsnap.intercepter.OpenAIIInterceptor
import com.home.mindsnap.repository.image.dao.openai.OpenAIGenService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

class RestModule {

    private val URL: String = "https://api.openai.com/v1/"
    private val TOKEN: String = ""

    //header값 지정해주는 okhttp client (oauth 사용을 위해 bearer 헤더 추가)
    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(OpenAIIInterceptor(URL, TOKEN)).build()
    }

    fun getOpenAIService() : OpenAIGenService {
        return Retrofit.Builder().client(getOkHttpClient()).baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build().create(OpenAIGenService::class.java)
    }



}