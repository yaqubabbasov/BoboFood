package com.yaqubabbasov.bobofood.ui.auth.login

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.data.datasource.PrefsManager
import com.yaqubabbasov.bobofood.databinding.FragmentLoginfragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private  var _binding: FragmentLoginfragmentBinding? =null
    private val  binding get() = _binding!!
    private lateinit var prefs: PrefsManager
    private val  viewmodel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       _binding= FragmentLoginfragmentBinding.inflate(inflater, container, false)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PrefsManager(requireContext())

        binding.signupbutton.setOnClickListener {
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.loginFragment) {
                navController.navigate(R.id.loginsignupbridge)
            }
        }
        binding.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }

        setupObserve()
        setupListener()

    }
    private fun setupObserve(){

        viewmodel.authstate.observe(viewLifecycleOwner) { result ->
            result.onSuccess { email ->
                Toast.makeText(requireContext(), "Welcome: $email", Toast.LENGTH_SHORT).show()

                lifecycleScope.launch {
                    prefs.setLoggedIn(true)
                    prefs.setOnboardingStep(4)
                }

                findNavController().navigate(R.id.homeFragment) // ya action id

            }.onFailure { e ->
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewmodel.emailError.observe(viewLifecycleOwner){error->
            binding.emaillayout.error=error
        }
        viewmodel.passwordError.observe(viewLifecycleOwner){error->
            binding.passwordlayout.error=error
        }


    }
    private fun setupListener(){

        binding.emailtext.addTextChangedListener {
            val email= it.toString()
            if (email.isEmpty()){
                binding.emaillayout.error=null
            }else{
                viewmodel.validateEmail(email)
                checkFields()
            } }
        binding.passwordtext.addTextChangedListener {
            viewmodel.validatePassword(it.toString())
            checkFields() }
         binding.loginbutton.setOnClickListener {
             val email=binding.emailtext.text.toString()
             val password=binding.passwordtext.text.toString()
             if (binding.loginbutton.isEnabled){
                 if (binding.loginbutton.isEnabled) {
                     viewmodel.login(email, password)
                 } else {
                     Toast.makeText(requireContext(),"Please check the information.", Toast.LENGTH_SHORT).show()
                 }

             }else{
                 Toast.makeText(requireContext(),"Please check the information.", Toast.LENGTH_SHORT).show()
             }
         }
    }
    fun checkFields() {
        val email = binding.emailtext.text.toString()
        val password = binding.passwordtext.text.toString()

        val isValid = email.isNotEmpty() && password.isNotEmpty()
        val backgroundcolor= ContextCompat.getColor(requireContext(),if(isValid) R.color.selectbutton  else R.color.unselectbutton)
        val textcolor= ContextCompat.getColor(requireContext(),if(isValid) R.color.white else R.color.buttontextcolor)


        binding.loginbutton.apply {
            backgroundTintList = ColorStateList.valueOf(backgroundcolor)
            setTextColor(textcolor)
            isEnabled = isValid
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }


}