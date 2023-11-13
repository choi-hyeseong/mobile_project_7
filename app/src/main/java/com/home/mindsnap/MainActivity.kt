package com.home.mindsnap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.home.mindsnap.databinding.ActivityMainBinding
import com.home.mindsnap.fragment.tutorial.WelcomeFragment

class MainActivity : AppCompatActivity(), ActivityCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        if (isFirstJoin())
            supportFragmentManager.beginTransaction().replace(R.id.frame, WelcomeFragment()).commit()

    }

    private fun isFirstJoin() : Boolean {
        //check user is first join
        //TODO
        return true
    }

    override fun saveUserTutorialEnded() {

    }
}