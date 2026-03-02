package com.yaqubabbasov.bobofood.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.yaqubabbasov.bobofood.data.datasource.PrefsManager
import com.yaqubabbasov.bobofood.databinding.FragmentOnBoarding1Binding
import com.yaqubabbasov.bobofood.ui.viewpager.OnboardingPageChanger
import com.yaqubabbasov.bobofood.ui.viewpager.ViewPagerFragment
import kotlinx.coroutines.launch

class OnBoarding1 : Fragment() {
    private lateinit var binding: FragmentOnBoarding1Binding
    private lateinit var prefs: PrefsManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= FragmentOnBoarding1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PrefsManager(requireContext())

        binding.nextbutton.setOnClickListener {
            Log.e("TEST", parentFragment.toString())
            lifecycleScope.launch {
                prefs.setOnboardingStep(2)
            }

            val changer = parentFragment as? OnboardingPageChanger
            changer?.goToPage(1)
        }


        binding.skipbutton.setOnClickListener {
            lifecycleScope.launch { prefs.setOnboardingStep(4) }
            findNavController().navigate(com.yaqubabbasov.bobofood.R.id.onBoarding4)
        }


    }



}