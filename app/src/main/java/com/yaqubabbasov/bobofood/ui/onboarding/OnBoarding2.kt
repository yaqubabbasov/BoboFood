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
import com.yaqubabbasov.bobofood.databinding.FragmentOnBoarding2Binding
import com.yaqubabbasov.bobofood.ui.viewpager.OnboardingPageChanger
import com.yaqubabbasov.bobofood.ui.viewpager.ViewPagerFragment
import kotlinx.coroutines.launch

class OnBoarding2 : Fragment() {
    private lateinit var binding: FragmentOnBoarding2Binding
    private lateinit var prefs: PrefsManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= FragmentOnBoarding2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PrefsManager(requireContext())
        binding.nextbutton.setOnClickListener {

            lifecycleScope.launch {
                prefs.setOnboardingStep(3)
            }

            val changer = parentFragment as? OnboardingPageChanger
            changer?.goToPage(2)
        }
        binding.skipbutton.setOnClickListener {
            lifecycleScope.launch {
                prefs.setOnboardingStep(4) // Skip → son onboarding
            }
            findNavController().navigate(R.id.onBoarding4) // ViewPager-dən çıx
        }

    }
    private fun onboardscreen(){
        val sharedpref=requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        val editor= sharedpref.edit()
        editor.putBoolean("finished",true)
        editor.apply()
    }


}