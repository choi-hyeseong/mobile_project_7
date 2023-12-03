package com.home.mindsnap.repository.image.dao.openai

import android.graphics.Bitmap
import com.home.mindsnap.component.BitmapGenerator
import com.home.mindsnap.repository.image.PromptGenerator
import com.home.mindsnap.repository.image.dao.ImageGenDao
import com.home.mindsnap.repository.image.dao.openai.request.ImageRequest
import com.home.mindsnap.repository.image.dao.openai.response.ImageResponse
import com.home.mindsnap.type.ArtStyle
import java.net.URL
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class OpenAIImageGenDao(private val generator : PromptGenerator, private val service : OpenAIGenService?, private val bitmapGenerator: BitmapGenerator) : ImageGenDao {
    //test를 위한 bitmapfactory 분리
    override suspend fun getImage(prompt: String, style: ArtStyle): Bitmap {
        val response = service!!.generateImage(ImageRequest(prompt = generator.generate(prompt, style)))
        return decodeImage(response)
    }

    //BitmapFactory는 static 메소드.. mock 오류가 나서 따로 런타입 테스트 거쳐서 잘 작동하는거 확인함.
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun decodeImage(response : ImageResponse) : Bitmap {
        val data = response.data[0]
        return if (data.url != null) {
            val connection = URL(data.url).openConnection()
            connection.connect()
            val input = connection.getInputStream()
            bitmapGenerator.decodeStream(input)
        }
        else {
            //url이 null이면 json은 not null
            val decodedString: ByteArray = Base64.decode(data.jsonData!!)
            bitmapGenerator.decodeByte(decodedString)
        }
    }
}