package com.home.mindsnap.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.home.mindsnap.type.ArtStyle

class PromptFragment : Fragment() {

    companion object {
        fun newInstance(prompt : String?, artStyle: ArtStyle?): PromptFragment {
            val args = Bundle()
            prompt?.let { args.putString(PROMPT, prompt) }
            artStyle?.let { args.putString(ARTSTYLE, artStyle.toString()) }
            return PromptFragment().also {
                it.arguments = args
            }
        }
    }
}