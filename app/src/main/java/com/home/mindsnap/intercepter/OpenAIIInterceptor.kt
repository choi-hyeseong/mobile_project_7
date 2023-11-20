package com.home.mindsnap.intercepter

import okhttp3.Interceptor
import okhttp3.Response

class OpenAIIInterceptor(private val url: String, private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain.request().newBuilder().addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer $token").build()
        return chain.proceed(request)
    }

}