package com.home.mindsnap.fragment.tutorial.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.home.mindsnap.databinding.ThirdLayoutBinding

class ThirdFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return ThirdLayoutBinding.inflate(inflater, container, false).root
    }
}