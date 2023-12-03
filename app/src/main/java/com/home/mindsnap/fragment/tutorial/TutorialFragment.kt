package com.home.mindsnap.fragment.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.home.mindsnap.databinding.TutorialLayoutBinding
import com.home.mindsnap.fragment.tutorial.pager.FirstFragment
import com.home.mindsnap.fragment.tutorial.pager.SecondFragment
import com.home.mindsnap.fragment.tutorial.pager.ThirdFragment

class TutorialFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val bind = TutorialLayoutBinding.inflate(inflater, container, false)
        bind.pager.adapter = FragmentAdapter(this)
        return bind.root
    }

    //view pager를 위한 adapter (fragment)
    inner class FragmentAdapter(tutorialFragment: TutorialFragment) : FragmentStateAdapter(tutorialFragment) {

        private val fragments = listOf(FirstFragment(), SecondFragment(), ThirdFragment())

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

        /*
        bind 할 필요 없는 이유 -> 기존 RecyclerView의 item은 data class의 형태. 따라서 각 아이템별로 bind가 필요했음.
        fragment -> 각자 클래스로 형성됨. 리스너 등록 가능.
         */
    }

}