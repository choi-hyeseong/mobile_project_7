package com.home.mindsnap.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.home.mindsnap.type.ArtStyle
import com.home.mindsnap.viewmodel.ResultViewModel

const val PROMPT = "PROMPT"
const val ARTSTYLE = "ARTSTYLE"
class ResultFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    companion object {
        fun newInstance(prompt : String, artStyle: ArtStyle): ResultFragment {
            val args = Bundle().apply {
                putString(PROMPT, prompt)
                putSerializable(ARTSTYLE, artStyle)
            }
            return ResultFragment().also {
                it.arguments = args
            }
        }
    }
}