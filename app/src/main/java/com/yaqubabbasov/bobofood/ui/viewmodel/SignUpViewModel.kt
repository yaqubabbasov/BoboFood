package com.yaqubabbasov.bobofood.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.integrity.r
import com.google.firebase.auth.FirebaseAuth
import com.yaqubabbasov.bobofood.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val prepo: ProductRepository): ViewModel() {
    private val _authstate = MutableLiveData<Result<String?>>()
    val authstate: LiveData<Result<String?>> get() = _authstate
    private val _currentuser = MutableLiveData<Result<String?>>()
    val currentuser: LiveData<Result<String?>> get() = _currentuser
    private val _validationState = MutableLiveData<String?>()
    val validationState: LiveData<String?> get() = _validationState
    private val _emailError=MutableLiveData<String?>()
    val emailError: LiveData<String?> get() = _emailError
    private val _passwordError=MutableLiveData<String?>()
    val passwordError: LiveData<String?> get() = _passwordError

    init {
       checkUserStatus()
    }

    fun registerauth(email: String, password: String) {
        viewModelScope.launch {
        if (!validateAll(email, password)) return@launch



      val result=  withContext(Dispatchers.IO){
          prepo.registerauth(email, password)

        }
            _authstate.value = result
    }}
        fun checkUserStatus() {
            _currentuser.postValue(prepo.getcurrentuser())
    }

    fun validateEmail(email: String){
        _emailError.value=when{
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                 "Düzgün email daxil edin"
                else->null

        }

    }
    fun validatePassword(password: String){
        _passwordError.value=when{
            password.isEmpty() -> null
            password.length < 8 -> "Şifrə ən azı 8 simvol olmalıdır"
            !password.any { it.isUpperCase() } -> "Ən azı bir böyük hərf olmalıdır"
            !password.any { it.isLowerCase() } -> "Ən azı bir kiçik hərf olmalıdır"
            !password.any { it.isDigit() } -> "Ən azı bir rəqəm olmalıdır"
            password.contains(" ") -> "Şifrədə boşluq ola bilməz"
            else -> null
        }
    }

     fun validateAll(email: String, password: String): Boolean {

         validateEmail(email)
         validatePassword(password)

        return when{
            _emailError.value!=null->false
            _passwordError.value!=null->false
            else-> true

        }


    }
}