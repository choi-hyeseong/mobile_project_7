package com.home.mindsnap.repository.image.dao.openai

import com.home.mindsnap.repository.image.dao.openai.request.ImageRequest
import com.home.mindsnap.repository.image.dao.openai.response.ImageResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAIGenService {

    //body 붙여야 request body 인식
    // request 요청시 영어로 올바른 문장을 써서 보내야 잘 인식함.
    @POST("images/generations")
    suspend fun generateImage(@Body request : ImageRequest) : ImageResponse
}