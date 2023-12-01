package com.home.mindsnap

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MindSnapApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}