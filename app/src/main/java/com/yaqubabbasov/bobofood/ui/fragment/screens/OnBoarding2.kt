package com.yaqubabbasov.bobofood.ui.fragment.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.databinding.FragmentOnBoarding2Binding
import com.yaqubabbasov.bobofood.ui.fragment.Loginfragment
import com.yaqubabbasov.bobofood.ui.fragment.ViewPagerFragment


class OnBoarding2 : Fragment() {
    private lateinit var binding: FragmentOnBoarding2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       binding= FragmentOnBoarding2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextbutton.setOnClickListener {
            ViewPagerFragment.viewpager?.currentItem=2
        }
        binding.skipbutton.setOnClickListener {
            onboardscreen()
            ViewPagerFragment.viewpager?.currentItem=3
        }

    }
    private fun onboardscreen(){
        val sharedpref=requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        val editor= sharedpref.edit()
        editor.putBoolean("finished",true)
        editor.apply()
    }


}