package com.home.mindsnap.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.mindsnap.ActivityCallback
import com.home.mindsnap.LOG_HEADER
import com.home.mindsnap.R
import com.home.mindsnap.databinding.ArtstyleLayoutBinding
import com.home.mindsnap.databinding.PromptLayoutBinding
import com.home.mindsnap.type.ArtStyle
import com.home.mindsnap.viewmodel.PromptViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromptFragment : Fragment() {

    private val viewModel: PromptViewModel by viewModels()
    private var callback: ActivityCallback? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = PromptLayoutBinding.inflate(layoutInflater, container, false).apply {
            recyclerView3.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            back.setOnClickListener { callback?.navigateToGallery() }
        }

        arguments?.apply {
            val prompt = getString(PROMPT, "")
            val artStyle = ArtStyle.fromString(getString(ARTSTYLE, ""))
            viewModel.setParameter(prompt, artStyle)
        }

        bind.generateBtn.setOnClickListener {
            viewModel.setParameter(bind.promptInput.text.toString(), null) //프롬프트 갱신
            viewModel.generate()
        }

        viewModel.promptLiveData.observe(viewLifecycleOwner) {
            bind.promptInput.setText(it)
        }

        viewModel.artStyleLiveData.observe(viewLifecycleOwner) {
            bind.recyclerView3.adapter = ArtStyleAdapter(it)
        }

        viewModel.responseLiveData.observe(viewLifecycleOwner) {
            it.getContent()?.let {data ->
                if (data.isResourceMessage())
                    Toast.makeText(requireContext(), data.getResourceMessage(requireContext()), Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.dataLiveData.observe(viewLifecycleOwner) {
            callback?.navigateToResult(it.first, it.second)
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

    //enum to recycler view, 나중에 enum을 모델로 묶으면 깔끔할듯
    inner class ArtStyleAdapter(private val artStyle: ArtStyle) :
        RecyclerView.Adapter<ArtStyleViewHolder>() {

        private val artStyles = ArtStyle.values()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtStyleViewHolder {
            return ArtStyleViewHolder(ArtstyleLayoutBinding.inflate(layoutInflater, parent, false))
        }

        override fun getItemCount(): Int {
            return artStyles.size
        }

        override fun onBindViewHolder(holder: ArtStyleViewHolder, position: Int) {
            holder.bind(artStyles[position], artStyle)
        }

    }

    inner class ArtStyleViewHolder(private val layout: ArtstyleLayoutBinding) :
        RecyclerView.ViewHolder(layout.root) {
        fun bind(currentStyle: ArtStyle, targetArtStyle: ArtStyle) {
            layout.artName.text = if (currentStyle == ArtStyle.NONE) "NONE" else currentStyle.toString()
            layout.styleImage.setImageDrawable(
                when (currentStyle) {
                    ArtStyle.FANTASY -> ContextCompat.getDrawable(requireContext(), R.drawable.rectangle_17)
                    ArtStyle.THIRD -> ContextCompat.getDrawable(requireContext(), R.drawable.rectangle_15)
                    ArtStyle.ARTISTIC -> ContextCompat.getDrawable(requireContext(), R.drawable.artistic)
                    ArtStyle.NONE -> ContextCompat.getDrawable(requireContext(), R.drawable.none)
                    ArtStyle.FAIRYTALE -> ContextCompat.getDrawable(requireContext(), R.drawable.fairytale)
                }
            )
            if (currentStyle == targetArtStyle)
                layout.checkBtn.visibility = View.VISIBLE
            else
                layout.styleImage.setOnClickListener {
                    viewModel.setParameter(null, currentStyle) //선택 안된경우 선택 되게
                }
        }
    }

    companion object {
        fun newInstance(prompt: String?, artStyle: ArtStyle?): PromptFragment {
            val args = Bundle()
            prompt?.let { args.putString(PROMPT, prompt) }
            artStyle?.let { args.putString(ARTSTYLE, artStyle.toString()) }
            return PromptFragment().also {
                it.arguments = args
            }
        }
    }
}