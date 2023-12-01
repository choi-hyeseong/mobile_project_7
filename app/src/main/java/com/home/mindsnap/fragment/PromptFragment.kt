package com.home.mindsnap.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.home.mindsnap.type.ArtStyle
import com.home.mindsnap.viewmodel.PromptViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromptFragment : Fragment() {

    private val viewModel : PromptViewModel by viewModels()
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