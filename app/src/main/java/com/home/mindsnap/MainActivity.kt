package com.home.mindsnap

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.home.mindsnap.databinding.ActivityMainBinding
import com.home.mindsnap.fragment.ARTSTYLE
import com.home.mindsnap.fragment.GalleryFragment
import com.home.mindsnap.fragment.PROMPT
import com.home.mindsnap.fragment.PromptFragment
import com.home.mindsnap.fragment.ResultFragment
import com.home.mindsnap.fragment.tutorial.CoachMarkFragment
import com.home.mindsnap.fragment.tutorial.TutorialFragment
import com.home.mindsnap.fragment.tutorial.WelcomeFragment
import com.home.mindsnap.type.ArtStyle
import com.home.mindsnap.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


const val LOG_HEADER = "MINDSNAP"
const val INTENT_IMAGE_GENERATE = "intent.IMAGE_GENERATE"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActivityCallback {

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        if (savedInstanceState == null) {
            //최초 한번만 인식해서 화면 회전은 무시
            viewModel.isFirstJoined().observe(this) { tutorial ->
                if (tutorial)
                    supportFragmentManager.beginTransaction().replace(R.id.frame, WelcomeFragment())
                        .commit()
                else {
                    //튜토리얼 완료되었을때만 인텐트 핸들링
                    //implicit intent
                    if (intent.hasExtra(PROMPT)) {
                        navigateToResult(
                            intent.getStringExtra(PROMPT)!!,
                            ArtStyle.fromString(intent.getStringExtra(ARTSTYLE) ?: "")
                        ) //has check, art style은 빈값이 올 수 있음
                    }
                    else if (intent.data != null) {
                        //deep link
                        // am start -W -a android.intent.action.VIEW -d "intent://genimage?prompt=a dog&artstyle=3D" com.home.mindsnap 로 호출가능
                        val uri = intent.data!!
                        val prompt = uri.getQueryParameter(PROMPT.lowercase())
                        val artStyle = uri.getQueryParameter(ARTSTYLE.lowercase()) ?: ""
                        if (prompt != null)
                            navigateToResult(prompt, ArtStyle.fromString(artStyle))
                        else {
                            Log.w(LOG_HEADER, "Deep link doesn't contain prompt parameter.")
                            navigateToGallery() //좀더 깔끔하게 안되나..
                        }
                    }
                    else
                        navigateToGallery()
                }
            }
        }
    }

    override fun navigateToTutorial() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, TutorialFragment()).commit()
    }

    override fun navigateToGallery() {
        supportFragmentManager.beginTransaction().replace(R.id.frame, GalleryFragment()).commit()
    }

    override fun navigateToPrompt(prompt: String?, artStyle: ArtStyle?) {
        val fragment: PromptFragment = PromptFragment.newInstance(prompt, artStyle)
        supportFragmentManager.beginTransaction().replace(R.id.frame, fragment).commit()

    }

    override fun requestFinish() {
        finish()
    }

    override fun navigateToCoach() {
        supportFragmentManager.beginTransaction().replace(R.id.frame, CoachMarkFragment()).commit()
    }

    override fun navigateToResult(prompt: String, artStyle: ArtStyle) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, ResultFragment.newInstance(prompt, artStyle)).commit()
    }
}