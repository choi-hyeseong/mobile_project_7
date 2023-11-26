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
import com.home.mindsnap.ActivityCallback
import com.home.mindsnap.LOG_HEADER
import com.home.mindsnap.databinding.ResultLayoutBinding
import com.home.mindsnap.dialog.LoadingDialog
import com.home.mindsnap.module.RestModule
import com.home.mindsnap.repository.gallery.LocalGalleryRepository
import com.home.mindsnap.repository.gallery.dao.LocalGalleryDao
import com.home.mindsnap.repository.image.OpenAIImageGenRepository
import com.home.mindsnap.repository.image.PromptGenerator
import com.home.mindsnap.repository.image.dao.openai.OpenAIImageGenDao
import com.home.mindsnap.type.ArtStyle
import com.home.mindsnap.usecase.GenerateImage
import com.home.mindsnap.usecase.SaveLocalImage
import com.home.mindsnap.usecase.ShareImage
import com.home.mindsnap.util.BitmapGenerator
import com.home.mindsnap.viewmodel.ResultViewModel

const val PROMPT = "PROMPT"
const val ARTSTYLE = "ARTSTYLE"

class ResultFragment : Fragment() {

    private var dialog: Dialog? = null //context bind시에만 활성화
    private var callback: ActivityCallback? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = ResultLayoutBinding.inflate(layoutInflater, container, false)
        val promptGenerator = PromptGenerator()
        val module = RestModule()
        val viewmodel = ResultViewModel(
            GenerateImage(
                OpenAIImageGenRepository(
                    OpenAIImageGenDao(
                        promptGenerator,
                        module.getOpenAIService(module.provideRetrofit()),
                        BitmapGenerator()))),
            SaveLocalImage(LocalGalleryRepository(LocalGalleryDao(requireContext().applicationContext))), ShareImage(requireContext()),
            promptGenerator)

        viewmodel.loadingLiveData.observe(viewLifecycleOwner) { loading ->
            if (loading)
                dialog?.show()
            else
                dialog?.hide()
        }

        viewmodel.resultImage.observe(viewLifecycleOwner) { image ->
            bind.resultImage.setImageBitmap(image)
        }

        viewmodel.toastLiveData.observe(viewLifecycleOwner) { event ->
            event.getContent()
                ?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
        }

        viewmodel.textLiveData.observe(viewLifecycleOwner) {text ->
            bind.resultPrompt.text = text
        }

        viewmodel.shareLiveData.observe(viewLifecycleOwner) {
            startActivity(it)
        }

        bind.resultShare.setOnClickListener {
            viewmodel.shareImage()
        }

        bind.resultDownload.setOnClickListener {
            viewmodel.saveImage()
        }

        bind.resultRetry.setOnClickListener {
            callback?.navigateToPrompt(bind.resultPrompt.text.toString()) //좀더 깔끔한 방법이 없을까라는 생각.. vm에서 처리할 수도 없음.
        }



        arguments?.apply {
            val prompt = getString(PROMPT, "")
            val artStyle = ArtStyle.fromString(getString(ARTSTYLE, ""))
            if (prompt.isNullOrEmpty()) {
                Log.e(LOG_HEADER, "prompt is null.")
                callback?.requestFinish()
            }
            viewmodel.setParameter(prompt, artStyle)
            viewmodel.generateImage()
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