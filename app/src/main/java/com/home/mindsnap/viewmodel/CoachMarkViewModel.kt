package com.home.mindsnap.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.mindsnap.usecase.SaveUserVisited
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoachMarkViewModel @Inject constructor(private val saveUserVisited: SaveUserVisited): ViewModel() {

    val tutorialLiveData : MutableLiveData<Boolean> = MutableLiveData()
    val coachLiveData : MutableLiveData<Int> = MutableLiveData(0) //카운트당 이미지 계산

    fun saveTutorialEnded() {
        saveUserVisited.saveVisited()
        tutorialLiveData.value = true
    }

    fun coachClick() {
        coachLiveData.value = coachLiveData.value?.plus(1)
    }
}