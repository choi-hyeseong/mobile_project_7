package com.home.mindsnap.fragment.tutorial.pager

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.home.mindsnap.ActivityCallback
import com.home.mindsnap.databinding.ThirdLayoutBinding

class ThirdFragment : Fragment() {

    private var callback : ActivityCallback? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return ThirdLayoutBinding.inflate(inflater, container, false).root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as ActivityCallback? //콜백 할당
    }

    override fun onDetach() {
        super.onDetach()
        callback = null //콜백 제거
    }
}