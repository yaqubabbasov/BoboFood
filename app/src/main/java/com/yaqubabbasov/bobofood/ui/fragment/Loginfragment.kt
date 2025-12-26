package com.yaqubabbasov.bobofood.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.databinding.FragmentLoginfragmentBinding
import com.yaqubabbasov.bobofood.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Loginfragment : Fragment() {
    private lateinit var binding: FragmentLoginfragmentBinding
    private val  viewmodel: LoginViewModel by viewModels ()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       binding= FragmentLoginfragmentBinding.inflate(inflater, container, false)





        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupbutton.setOnClickListener {
            findNavController().navigate(R.id.loginsignupbridge)
        }
        setupObserve()
        setupListener()

    }
    private fun setupObserve(){
        viewmodel.currentuser.observe(viewLifecycleOwner){result->
            result.onSuccess {email->
                if (email!==null){
                    Toast.makeText(requireContext(),"Oturum açık:$email",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),"Oturum kapalı!!",Toast.LENGTH_SHORT).show()
                }

            }.onFailure{
                Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
            }

        }
        viewmodel.authstate.observe(viewLifecycleOwner){result ->
            result.onSuccess {
                Toast.makeText(requireContext(),"Welcome:$it",Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.loghomebridge)
            }.onFailure {exception ->
                Toast.makeText(requireContext(),exception.message,Toast.LENGTH_SHORT).show()
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
                 viewmodel.login(email,password)
             }else{
                 Toast.makeText(requireContext(),"Lütfen bilgileri kontrol edin",Toast.LENGTH_SHORT).show()
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


}