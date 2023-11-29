package com.home.mindsnap.viewmodel

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.mindsnap.R
import com.home.mindsnap.component.StringData
import com.home.mindsnap.event.Event
import com.home.mindsnap.repository.image.PromptGenerator
import com.home.mindsnap.type.ArtStyle
import com.home.mindsnap.usecase.ExistImage
import com.home.mindsnap.usecase.GenerateImage
import com.home.mindsnap.usecase.SaveLocalImage
import com.home.mindsnap.usecase.ShareImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ResultViewModel(
    private val generateImage: GenerateImage,
    private val saveLocalImage: SaveLocalImage,
    private val existImage : ExistImage,
    private val shareImage: ShareImage,
    private val promptGenerator: PromptGenerator
) : ViewModel() {

    val resultImage: MutableLiveData<Bitmap> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val toastLiveData: MutableLiveData<Event<StringData>> = MutableLiveData()
    val textLiveData: MutableLiveData<String> = MutableLiveData()
    val shareLiveData: MutableLiveData<Intent> = MutableLiveData()
    private val fileName: String by lazy {
        promptGenerator.generate(prompt, artStyle) //fileName 가져올때는 prompt와 style 존재
    }


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

    fun shareImage() {
        if (existImage.existImage(fileName))
            shareLiveData.value = shareImage.shareImage(fileName)
        else
            toastLiveData.postValue(Event(StringData(R.string.before_download, null)))
    }

    fun saveImage() {
        CoroutineScope(Dispatchers.IO).launch {
            resultImage.value?.let {
                saveLocalImage.saveImage(it, fileName)
                toastLiveData.postValue(Event(StringData(R.string.save_complete, null)))
            }
        }
    }

    fun setParameter(prompt: String, artStyle: ArtStyle) {
        this.prompt = prompt
        this.artStyle = artStyle
        notifyPromptChange(prompt)
    }

    private fun notifyPromptChange(prompt: String) {
        textLiveData.value = prompt
    }
}