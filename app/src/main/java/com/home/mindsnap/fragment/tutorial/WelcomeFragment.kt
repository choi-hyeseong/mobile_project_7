package com.home.mindsnap.fragment.tutorial

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.home.mindsnap.R
import com.home.mindsnap.databinding.WelcomeLayoutBinding
import com.home.mindsnap.dialog.LoadingDialog
import kotlin.concurrent.thread

class WelcomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = WelcomeLayoutBinding.inflate(layoutInflater, container, false)
        val textView = bind.title
        //textview 일정 부분만 스타일 적용하는 빌더
        val builder = SpannableStringBuilder(textView.text.toString())
        val span = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.light_purple))
        builder.setSpan(span, 14,19, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        textView.text = builder
        bind.button.setOnClickListener {
            //dialog test
            val dialog = LoadingDialog(requireContext())
            dialog.show()
            thread {
                SystemClock.sleep(2000) //메인 쓰레드 블락하면 안보임
                requireActivity().runOnUiThread {
                    dialog.hide()
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frame, TutorialFragment()).commit()
                }
            }

        }

        return bind.root
    }
}