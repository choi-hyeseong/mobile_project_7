package com.home.mindsnap.repository.image.dao.openai.response

import com.google.gson.annotations.SerializedName

data class ImageDataResponse(@SerializedName("b64_json") val jsonData : String?, @SerializedName("url") val url : String?)