package com.home.mindsnap.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment

class PromptFragment : Fragment() {

    companion object {
        fun newInstance(prompt : String?): PromptFragment {
            val args = Bundle()
            prompt?.let { args.putString(PROMPT, prompt) }
            return PromptFragment().also {
                it.arguments = args
            }
        }
    }
}