package com.yaqubabbasov.bobofood.ui.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yaqubabbasov.bobofood.ui.onboarding.OnBoarding1
import com.yaqubabbasov.bobofood.ui.onboarding.OnBoarding2
import com.yaqubabbasov.bobofood.ui.onboarding.OnBoarding3
class ViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    private val fragList = listOf(
        OnBoarding1(),
        OnBoarding2(),
        OnBoarding3()
    )

    override fun getItemCount() = fragList.size

    override fun createFragment(position: Int): Fragment {
        return fragList[position]
    }
}