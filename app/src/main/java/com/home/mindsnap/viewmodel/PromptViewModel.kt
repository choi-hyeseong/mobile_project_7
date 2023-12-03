package com.home.mindsnap.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.mindsnap.R
import com.home.mindsnap.component.StringData
import com.home.mindsnap.event.Event
import com.home.mindsnap.type.ArtStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PromptViewModel @Inject constructor() : ViewModel() {

    //intent로 받은 프롬프트와 스타일.
    private var prompt: String = ""
    private var artStyle: ArtStyle = ArtStyle.NONE

    val artStyleLiveData: MutableLiveData<ArtStyle> = MutableLiveData()
    val promptLiveData: MutableLiveData<String> = MutableLiveData()
    val responseLiveData : MutableLiveData<Event<StringData>> = MutableLiveData()
    val dataLiveData : MutableLiveData<Pair<String, ArtStyle>> = MutableLiveData() //프래그먼트 전환.

    fun setParameter(prompt: String?, artStyle: ArtStyle?) {
        prompt?.let {
            this.prompt = it
            notifyPromptUpdate()
        }
        artStyle?.let {
            this.artStyle = it
            notifyArtStyleUpdate()
        }
    }

    fun generate() {
        if (prompt.isEmpty())
            responseLiveData.value = Event(StringData(R.string.prompt_empty, null))
        else
            dataLiveData.value = Pair(prompt, artStyle)
    }

    private fun notifyPromptUpdate() {
        promptLiveData.value = prompt
    }

    private fun notifyArtStyleUpdate() {
        artStyleLiveData.value = artStyle
    }
}