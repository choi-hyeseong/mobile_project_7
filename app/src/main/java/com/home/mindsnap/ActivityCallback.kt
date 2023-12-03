package com.home.mindsnap

import com.home.mindsnap.type.ArtStyle

interface ActivityCallback {

    fun navigateToTutorial()

    fun navigateToCoach()

    //튜토리얼 완료해서 프래그먼트 이동
    fun navigateToGallery()

    fun navigateToResult(prompt : String, artStyle: ArtStyle)

    fun navigateToPrompt() {
        navigateToPrompt(null, null)
    }

    fun requestFinish()

    fun navigateToPrompt(prompt: String?, artStyle: ArtStyle?)

}