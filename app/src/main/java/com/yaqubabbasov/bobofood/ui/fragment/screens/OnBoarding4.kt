package com.yaqubabbasov.bobofood.ui.fragment.screens

import android.content.Context
import android.credentials.CredentialManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.databinding.FragmentOnBoarding4Binding
import com.yaqubabbasov.bobofood.ui.fragment.HomeFragment
import com.yaqubabbasov.bobofood.ui.fragment.Loginfragment
import com.yaqubabbasov.bobofood.ui.viewmodel.GoogleSigninViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoarding4 : Fragment() {
    private lateinit var binding: FragmentOnBoarding4Binding
    private val viewmodel: GoogleSigninViewModel by viewModels ()
 
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentOnBoarding4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth= FirebaseAuth.getInstance()


        with(binding){
            hamburgerlottie.setMinProgress(0.0f)
            hamburgerlottie.setMaxProgress(1.0f)
            hamburgerlottie.repeatCount=3
            hamburgerlottie.repeatMode= LottieDrawable.RESTART
            hamburgerlottie.playAnimation()
        }


        fun onboardseen(){
            val sharedpref=requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            val editor= sharedpref.edit()
            editor.putBoolean("finished",true)
            editor.apply()
        }


       val googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {result->
                   viewmodel.firebaseAuthwithGoogle(result)
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
        viewmodel.authstate.observe(viewLifecycleOwner){result->
            result.onSuccess {
              val homefragment= HomeFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.navhostfr, homefragment)
                    .addToBackStack(null)
                    .commit()
            }
            result.onFailure{ exception->
                Toast.makeText(requireContext(), "Google sign-in failed: ${exception.message}", Toast.LENGTH_SHORT).show()

            }

        }
        binding.logbutton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.navhostfr, Loginfragment())
                commit()
            }
        }



    }



}
