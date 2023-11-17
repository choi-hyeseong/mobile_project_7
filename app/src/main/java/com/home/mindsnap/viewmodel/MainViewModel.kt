package com.home.mindsnap.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.mindsnap.usecase.GetUserFirstJoined

class MainViewModel(private val getUserFirstJoined: GetUserFirstJoined) : ViewModel() {

    fun isFirstJoined() : LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        liveData.value = getUserFirstJoined.isFirstJoined()
        return liveData
    }

}