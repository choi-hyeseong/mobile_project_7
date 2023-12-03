package com.home.mindsnap.fragment.tutorial

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.home.mindsnap.ActivityCallback
import com.home.mindsnap.R
import com.home.mindsnap.databinding.CoachLayoutBinding
import com.home.mindsnap.viewmodel.CoachMarkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoachMarkFragment : Fragment() {

    private val viewModel: CoachMarkViewModel by viewModels()
    private var callback: ActivityCallback? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val bind = CoachLayoutBinding.inflate(layoutInflater, container, false)

        bind.btnStart.setOnClickListener {
            viewModel.saveTutorialEnded()
        }

        viewModel.tutorialLiveData.observe(viewLifecycleOwner) {
            callback?.navigateToGallery()
        }

        viewModel.coachLiveData.observe(viewLifecycleOwner) {
            val drawable: Drawable? = when (it) {
                1 -> ContextCompat.getDrawable(requireContext(), R.drawable.coach_2)
                2 -> ContextCompat.getDrawable(requireContext(), R.drawable.coach_3)
                3 -> ContextCompat.getDrawable(requireContext(), R.drawable.coach_4)
                4 -> ContextCompat.getDrawable(requireContext(), R.drawable.coach_5)
                5 -> ContextCompat.getDrawable(requireContext(), R.drawable.coach_6)
                6 -> ContextCompat.getDrawable(requireContext(), R.drawable.coach_7)
                else -> {
                    //화면 회전시 뷰 안뜨는 문제 해결
                    if (it != 0)
                        bind.btnStart.visibility = View.VISIBLE
                    null
                }
            }
            drawable?.let { bind.coachImage.setImageDrawable(it) }
        }

        bind.coachImage.setOnClickListener {
            viewModel.coachClick()
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