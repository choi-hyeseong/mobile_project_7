package com.home.mindsnap.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.mindsnap.event.Event
import com.home.mindsnap.repository.image.PromptGenerator
import com.home.mindsnap.type.ArtStyle
import com.home.mindsnap.usecase.GenerateImage
import com.home.mindsnap.usecase.SaveLocalImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultViewModel(
    private val generateImage: GenerateImage,
    private val saveLocalImage: SaveLocalImage,
    private val promptGenerator: PromptGenerator
) : ViewModel() {

    val resultImage: MutableLiveData<Bitmap> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val toastLiveData: MutableLiveData<Event<String>> = MutableLiveData()

    //intent로 받은 프롬프트와 스타일.
    private var prompt: String = ""
    private var artStyle: ArtStyle = ArtStyle.NONE
    fun generateImage() {
        CoroutineScope(Dispatchers.IO).launch {
            loadingLiveData.postValue(true)
            resultImage.postValue(generateImage.generateImage(prompt, artStyle))
            loadingLiveData.postValue(false)
        }
    }

    fun saveImage() {
        CoroutineScope(Dispatchers.IO).launch {
            resultImage.value?.let {
                saveLocalImage.saveImage(it, promptGenerator.generate(prompt, artStyle))
                toastLiveData.postValue(Event("이미지 저장이 완료되었습니다.")) //TODO
            }
        }
    }

    fun setParameter(prompt : String, artStyle: ArtStyle) {
        this.prompt = prompt
        this.artStyle = artStyle
    }
}