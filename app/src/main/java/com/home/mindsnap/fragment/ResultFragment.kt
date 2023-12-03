package com.home.mindsnap.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.home.mindsnap.ActivityCallback
import com.home.mindsnap.LOG_HEADER
import com.home.mindsnap.databinding.ResultLayoutBinding
import com.home.mindsnap.dialog.LoadingDialog
import com.home.mindsnap.type.ArtStyle
import com.home.mindsnap.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint

const val PROMPT = "PROMPT"
const val ARTSTYLE = "ARTSTYLE"

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private var dialog: Dialog? = null //context bind시에만 활성화
    private var callback: ActivityCallback? = null
    private val viewModel : ResultViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = ResultLayoutBinding.inflate(layoutInflater, container, false)
        viewModel.loadingLiveData.observe(viewLifecycleOwner) { loading ->
            if (loading)
                dialog?.show()
            else
                dialog?.dismiss() //dismiss로 해야 ui 안가리고 작동됨.
        }

        viewModel.resultImage.observe(viewLifecycleOwner) { image ->
            bind.resultImage.setImageBitmap(image)
        }

        viewModel.toastLiveData.observe(viewLifecycleOwner) { event ->
            event.getContent()?.let {
                val message =
                    if (it.isResourceMessage()) it.getResourceMessage(requireContext()) else it.message
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.textLiveData.observe(viewLifecycleOwner) { text ->
            bind.resultPrompt.text = text
        }

        viewModel.shareLiveData.observe(viewLifecycleOwner) {
            startActivity(it)
        }

        viewModel.retryLiveData.observe(viewLifecycleOwner) { pair ->
            callback?.navigateToPrompt(pair.first, pair.second)
        }

        bind.resultShare.setOnClickListener {
            viewModel.shareImage()
        }

        bind.resultDownload.setOnClickListener {
            viewModel.saveImage()
        }

        bind.resultRetry.setOnClickListener {
            viewModel.retryPrompt()
        }



        arguments?.apply {
            val prompt = getString(PROMPT, "")
            val artStyle = ArtStyle.fromString(getString(ARTSTYLE, ""))
            if (prompt.isNullOrEmpty()) {
                Log.e(LOG_HEADER, "prompt is null.")
                callback?.requestFinish()
            }
            viewModel.setParameter(prompt, artStyle)
            viewModel.generateImage()
        }

        return bind.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as ActivityCallback?
        dialog?.hide()
        dialog = LoadingDialog(context)
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
        dialog?.hide()
        dialog = null
    }

    companion object {
        fun newInstance(prompt: String, artStyle: ArtStyle): ResultFragment {
            val args = Bundle().apply {
                putString(PROMPT, prompt)
                putString(ARTSTYLE, artStyle.toString())
            }
            return ResultFragment().also {
                it.arguments = args
            }
        }
    }
}