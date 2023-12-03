package com.home.mindsnap.fragment.tutorial

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.home.mindsnap.ActivityCallback
import com.home.mindsnap.R
import com.home.mindsnap.databinding.WelcomeLayoutBinding

class WelcomeFragment : Fragment() {

    private var callback : ActivityCallback? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = WelcomeLayoutBinding.inflate(layoutInflater, container, false)
        val textView = bind.title
        //textview 일정 부분만 스타일 적용하는 빌더
        val builder = SpannableStringBuilder(textView.text.toString())
        val span =
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.light_purple))
        builder.setSpan(span, 14, 19, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        textView.text = builder
        bind.button.setOnClickListener {
            callback?.navigateToTutorial()
        }

        return bind.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as ActivityCallback?
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
}