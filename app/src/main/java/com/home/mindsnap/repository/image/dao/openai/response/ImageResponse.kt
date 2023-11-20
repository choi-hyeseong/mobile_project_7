package com.home.mindsnap.repository.image.dao.openai.response

import com.google.gson.annotations.SerializedName

data class ImageResponse(@SerializedName("b64_json") val base64Json: String, @SerializedName("revised_prompt") val revisedPrompt: String)