package com.yaqubabbasov.bobofood.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.data.datasource.PrefsManager
import com.yaqubabbasov.bobofood.databinding.FragmentOnBoarding4Binding
import com.yaqubabbasov.bobofood.ui.home.HomeFragment
import com.yaqubabbasov.bobofood.ui.auth.GoogleSigninViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoarding4 : Fragment() {
    private lateinit var binding: FragmentOnBoarding4Binding
    private lateinit var prefs: PrefsManager
    private val viewmodel: GoogleSigninViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= FragmentOnBoarding4Binding.inflate(inflater, container, false)
        return binding.root
    }

    fun onboardseen(){
        val sharedpref=requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        val editor= sharedpref.edit()
        editor.putBoolean("finished",true)
        editor.apply()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PrefsManager(requireContext())
        auth= FirebaseAuth.getInstance()


        with(binding){
            hamburgerlottie.setMinProgress(0.0f)
            hamburgerlottie.setMaxProgress(1.0f)
            hamburgerlottie.repeatCount=3
            hamburgerlottie.repeatMode= LottieDrawable.RESTART
            hamburgerlottie.playAnimation()
        }





       val googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {result->
                    viewmodel.firebaseAuthwithGoogle(result)
                    lifecycleScope.launch {
                        prefs.setLoggedIn(true)     // Google login oldu
                        prefs.setOnboardingStep(4)  // OnBoarding4 tamamlandı
                        findNavController().navigate(R.id.homeFragment)
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }








        binding.googlebutton.setOnClickListener {
             val signinclient=viewmodel.getGoogleSignİnclient(requireContext())

            signinclient.signOut().addOnCompleteListener {
                googleSignInLauncher.launch(signinclient.signInIntent)
            }


        }

        viewmodel.authstate.observe(viewLifecycleOwner){ result: Result<String?> ->
            result.onSuccess {

                lifecycleScope.launch {
                    prefs.setLoggedIn(true)
                }

                findNavController().navigate(R.id.homeFragment)
            }

            result.onFailure { exception ->
                Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
            }
        }


        binding.logbutton.setOnClickListener {
            findNavController().navigate(R.id.onborading4bridgelogin)
        }





    }



}