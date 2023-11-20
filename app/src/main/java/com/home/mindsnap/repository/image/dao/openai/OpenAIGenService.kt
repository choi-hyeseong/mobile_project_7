package com.home.mindsnap.repository.image.dao.openai

import com.home.mindsnap.repository.image.dao.openai.request.ImageRequest
import com.home.mindsnap.repository.image.dao.openai.response.ImageResponse
import retrofit2.http.POST

interface OpenAIGenService {

    @POST("images/generations")
    suspend fun generateImage(request : ImageRequest) : ImageResponse
}