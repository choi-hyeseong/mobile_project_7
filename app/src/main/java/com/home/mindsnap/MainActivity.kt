package com.home.mindsnap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.home.mindsnap.databinding.ActivityMainBinding
import com.home.mindsnap.fragment.GalleryFragment
import com.home.mindsnap.fragment.tutorial.WelcomeFragment
import com.home.mindsnap.repository.user.PreferenceUserRepository
import com.home.mindsnap.repository.user.dao.PreferenceUserDao
import com.home.mindsnap.usecase.GetUserFirstJoined
import com.home.mindsnap.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), ActivityCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        val viewModel = MainViewModel(
            GetUserFirstJoined(
                PreferenceUserRepository(
                    PreferenceUserDao(getSharedPreferences("test", MODE_PRIVATE)))))
        viewModel.isFirstJoined().observe(this) { tutorial ->
            if (tutorial)
                supportFragmentManager.beginTransaction().replace(R.id.frame, WelcomeFragment())
                    .commit()
            else
                navigateToGallery()
        }
    }

    override fun navigateToGallery() {
        supportFragmentManager.beginTransaction().replace(R.id.frame, GalleryFragment()).commit()
    }
}