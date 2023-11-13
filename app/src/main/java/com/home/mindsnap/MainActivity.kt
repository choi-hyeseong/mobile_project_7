package com.home.mindsnap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.home.mindsnap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ActivityCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        // TODO if (isFirstJoin())

    }

    private fun isFirstJoin() : Boolean {
        //check user is first join
        //TODO
        return true
    }

    override fun saveUserTutorialEnded() {

    }
}