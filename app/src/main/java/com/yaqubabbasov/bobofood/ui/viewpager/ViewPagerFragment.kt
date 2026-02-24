package com.yaqubabbasov.bobofood.ui.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.yaqubabbasov.bobofood.databinding.FragmentViewPagerBinding
import com.yaqubabbasov.bobofood.ui.onboarding.OnBoarding1
import com.yaqubabbasov.bobofood.ui.viewpager.ViewPagerAdapter

class ViewPagerFragment : Fragment(), OnboardingPageChanger {

    private lateinit var binding: FragmentViewPagerBinding
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ViewPagerAdapter(this)   // 🔥 BURASI ÇOX VACİB
        binding.viewpager.adapter = adapter
    }

    override fun goToPage(page: Int) {
        binding.viewpager.currentItem = page
    }
}