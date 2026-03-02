package com.yaqubabbasov.bobofood.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.data.datasource.PrefsManager
import com.yaqubabbasov.bobofood.databinding.FragmentOnBoarding3Binding
import kotlinx.coroutines.launch

class OnBoarding3 : Fragment() {
    private lateinit var binding: FragmentOnBoarding3Binding
    private lateinit var prefs: PrefsManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= FragmentOnBoarding3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PrefsManager(requireContext())
        binding.nextbutton.setOnClickListener {
            lifecycleScope.launch {
                prefs.setOnboardingStep(4)
            }
            findNavController().navigate(R.id.onBoarding4)
        }

        binding.skipbutton.setOnClickListener {
            lifecycleScope.launch {
                prefs.setOnboardingStep(4)
            }
            findNavController().navigate(R.id.onBoarding4)
        }
    }



}