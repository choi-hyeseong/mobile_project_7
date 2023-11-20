package com.home.mindsnap.repository.image.dao.openai.request

import com.google.gson.annotations.SerializedName

data class ImageRequest(
    //request dto. serialized name 미 지정시 json 컨버팅중 오류 발생
    @SerializedName("model")
    val model: String = "dall-e-2",
    @SerializedName("prompt")
    val prompt: String,
    @SerializedName("n")
    val n: Int = 1,
    @SerializedName("size")
    val size: String = "1024x1024",
    @SerializedName("response_format")
    val responseFormat : String = "b64_json" //url도 가능
)