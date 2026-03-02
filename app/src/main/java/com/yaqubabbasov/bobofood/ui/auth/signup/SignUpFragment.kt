package com.yaqubabbasov.bobofood.ui.auth.signup

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
import androidx.navigation.fragment.findNavController
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.util.getPasswordStrength
import com.yaqubabbasov.bobofood.databinding.FragmentSignUpfragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
 private var _binding: FragmentSignUpfragmentBinding?=null
    private val binding get() = _binding!!
 private val viewmodel: SignUpViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       _binding= FragmentSignUpfragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserve()
        setupListener()
        passwordstrength()
        binding.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }



    }
    private fun setupObserve(){
        viewmodel.currentuser.observe(viewLifecycleOwner){result->
            result.onSuccess {email->
                if (email!=null){
                    Toast.makeText(requireContext(),"Signed in: $email", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),"Signed out!!", Toast.LENGTH_SHORT).show()
                }

            }.onFailure{
                Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
            }

        }
        viewmodel.authstate.observe(viewLifecycleOwner){result ->
            result.onSuccess {
                findNavController().navigate(R.id.signloginbridge)
                Toast.makeText(requireContext(),it, Toast.LENGTH_SHORT).show()
            }.onFailure {exception ->
                Toast.makeText(requireContext(),exception.message, Toast.LENGTH_SHORT).show()
            }


        }
        viewmodel.usernameError.observe(viewLifecycleOwner) { error ->
            binding.usernamelayout.error = error
        }
       viewmodel.emailError.observe(viewLifecycleOwner){error->
           binding.emaillayout.error=error
       }
        viewmodel.passwordError.observe(viewLifecycleOwner){error->
            binding.passwordlayout.error=error
        }
        viewmodel.validationState.observe(viewLifecycleOwner){error->
            binding.signcheckbox.error=error
        }

    }
    private fun setupListener(){
        binding.usernametext.addTextChangedListener {
            viewmodel.validateUsername(it.toString())
            checkFields()
        }

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
        binding.signcheckbox.setOnCheckedChangeListener { _,_->
            checkFields()}

        binding.loginbutton.setOnClickListener {
            val email=binding.emailtext.text.toString()
            val password=binding.passwordtext.text.toString()
            val username= binding.usernametext.text.toString().trim()
            if (username.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a username.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val checkbox= binding.signcheckbox.isChecked
            if (!checkbox) {
                Toast.makeText(requireContext(), "You must accept the terms and conditions.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener   // stop here, don't navigate
            }
            val isvalid= viewmodel.validateAll(email,password,username)
            if (isvalid){
                viewmodel.registerauth(email,password, username)
            }else{
                    Toast.makeText(requireContext(),"Please check the information.", Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun passwordstrength(){
        binding.passwordtext.addTextChangedListener { editable ->
            val password = editable.toString()
            if (password.isEmpty()) {
                binding.passwordStrengthBar.progress = 0
                binding.passwordStrengthBar.progressTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.unselectbutton))
                binding.passwordStrengthLabel.text = ""
                return@addTextChangedListener}


            val strength = getPasswordStrength(password)

            binding.passwordStrengthBar.progress = strength.level
            binding.passwordStrengthBar.progressTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), strength.color))

            binding.passwordStrengthLabel.text = strength.label
            binding.passwordStrengthLabel.setTextColor(ContextCompat.getColor(requireContext(), strength.color))
        }
    }
    fun checkFields() {
        val username = binding.usernametext.text.toString().trim()
        val email = binding.emailtext.text.toString()
        val password = binding.passwordtext.text.toString()
        val ischecked= binding.signcheckbox.isChecked

        val isValid = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && ischecked

        val backgroundcolor= ContextCompat.getColor(requireContext(),if(isValid) R.color.selectbutton  else R.color.unselectbutton)
        val textcolor= ContextCompat.getColor(requireContext(),if(isValid) R.color.white else R.color.buttontextcolor)


        binding.loginbutton.apply {
            backgroundTintList = ColorStateList.valueOf(backgroundcolor)
            setTextColor(textcolor)
                //  isEnabled = isValid
        }

    }






}