package com.yaqubabbasov.bobofood.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yaqubabbasov.bobofood.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel@Inject constructor(val prepo: ProductRepository): ViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _currentuser = MutableLiveData<Result<String?>>()
    val authstate: LiveData<Result<String?>> get() = _authstate
    private val _authstate = MutableLiveData<Result<String?>>()
    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> get() = _emailError
    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> get() = _passwordError



    fun login(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            val userEmail = auth.currentUser?.email
            _authstate.postValue(Result.success(userEmail))
            checkuserstatus()
        } catch (e: Exception) {
            _authstate.postValue(Result.failure(e))
        }

    }

    private fun checkuserstatus() {
        auth.currentUser?.let {
            _currentuser.postValue(Result.success(it.email))
        } ?: _currentuser.postValue(Result.success(null))
    }



    fun validateEmail(email: String) {
        _emailError.value = when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Please enter a valid email address."
            email.isEmpty()->"Please enter a valid email address."

            else -> null

        }

    }

    fun validatePassword(password: String) {
        _passwordError.value = when {
            password.isEmpty() -> null
            password.length < 8 -> "The password must be at least 8 characters long."
            password.contains(" ") -> "The password cannot contain spaces."
            else -> null
        }
    }


}