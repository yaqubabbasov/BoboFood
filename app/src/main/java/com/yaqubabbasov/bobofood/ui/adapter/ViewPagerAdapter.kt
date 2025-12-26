package com.yaqubabbasov.bobofood.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yaqubabbasov.bobofood.ui.fragment.screens.OnBoarding1
import com.yaqubabbasov.bobofood.ui.fragment.screens.OnBoarding2
import com.yaqubabbasov.bobofood.ui.fragment.screens.OnBoarding3
import com.yaqubabbasov.bobofood.ui.fragment.screens.OnBoarding4

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    val fraglist= listOf(OnBoarding1(), OnBoarding2(), OnBoarding3(), OnBoarding4())
    override fun createFragment(position: Int): Fragment {
        return fraglist[position]
    }

    override fun getItemCount(): Int {
      return fraglist.size
    }
}