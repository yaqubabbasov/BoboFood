package com.yaqubabbasov.bobofood.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.google.firebase.auth.FirebaseAuth
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        /* lifecycleScope.launch {
             delay(4000)
             findNavController().navigate(R.id.splash_viewpagerbridge)
         }*/
        val courierlottie= binding.courierlottie
        with(binding){
                courierlottie.setMinProgress(0.0f)
                courierlottie.setMaxProgress(1.0f)
                courierlottie.repeatCount=3
                courierlottie.repeatMode=LottieDrawable.RESTART
                courierlottie.playAnimation()
        }
        return binding.root

}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        val finished = sharedPref.getBoolean("finished", false)
        auth=FirebaseAuth.getInstance()
        val currentUser=auth.currentUser

        view.postDelayed({
            val navController = findNavController()

            if (!finished) {
                // Onboarding bitməyibsə → ViewPager
                navController.navigate(R.id.splash_viewpagerbridge)
            } else {
                if (currentUser != null) {
                    // User artıq login olub → Home
                    navController.navigate(R.id.splash_home_bridge)
                } else {
                    // Onboarding bitib, amma login olmayıb → Login
                    navController.navigate(R.id.splash_onboarding4_bridge)
                }
            }
        }, 4000) // Splash 4 saniyə görünəcək// Splash 2 saniyə görünəcək
    }


}




