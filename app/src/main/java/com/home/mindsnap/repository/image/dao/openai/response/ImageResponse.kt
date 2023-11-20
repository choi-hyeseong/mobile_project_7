package com.home.mindsnap.repository.image.dao.openai.response

import com.google.gson.annotations.SerializedName

data class ImageResponse(@SerializedName("created") val created : Long, @SerializedName("data") val data : List<ImageDataResponse>)