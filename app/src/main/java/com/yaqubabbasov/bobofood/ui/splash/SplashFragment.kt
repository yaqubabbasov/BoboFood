package com.yaqubabbasov.bobofood.ui.splash

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.google.firebase.auth.FirebaseAuth
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.data.datasource.PrefsManager
import com.yaqubabbasov.bobofood.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private  var _binding: FragmentSplashBinding?=null
    private val binding get() = _binding!!
    private lateinit var prefs: PrefsManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)


        return binding.root

}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PrefsManager(requireContext())

        val courierlottie = binding.courierlottie
        with(binding) {
            courierlottie.setMinProgress(0.0f)
            courierlottie.setMaxProgress(1.0f)
            courierlottie.repeatCount = 3
            courierlottie.repeatMode = LottieDrawable.RESTART
            courierlottie.playAnimation()
        }






        viewLifecycleOwner.lifecycleScope.launch {

            val step = prefs.getOnboardingStep()
            val logged = prefs.isLoggedIn()
            delay(3000)


            when {

                logged -> {
                    findNavController().navigate(R.id.splash_home_bridge)
                }

                step == 4 -> {
                    findNavController().navigate(R.id.splash_onboarding4_bridge)
                }

                else -> {
                    findNavController().navigate(R.id.viewPagerFragment)
                }
            }
        }


    }

}




