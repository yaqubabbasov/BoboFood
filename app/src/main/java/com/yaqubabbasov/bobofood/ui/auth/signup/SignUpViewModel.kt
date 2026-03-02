package com.yaqubabbasov.bobofood.ui.auth.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaqubabbasov.bobofood.data.datasource.PrefsManager
import com.yaqubabbasov.bobofood.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val prepo: ProductRepository,
                                          val dataStoreManager: PrefsManager
): ViewModel() {
    private val _authstate = MutableLiveData<Result<String?>>()
    val authstate: LiveData<Result<String?>> get() = _authstate
    private val _currentuser = MutableLiveData<Result<String?>>()
    val currentuser: LiveData<Result<String?>> get() = _currentuser
    private val _validationState = MutableLiveData<String?>()
    val validationState: LiveData<String?> get() = _validationState
    private val _emailError= MutableLiveData<String?>()
    val emailError: LiveData<String?> get() = _emailError
    private val _passwordError= MutableLiveData<String?>()
    val passwordError: LiveData<String?> get() = _passwordError
    private val _usernameError = MutableLiveData<String?>()
    val usernameError: LiveData<String?> = _usernameError


    init {
       checkUserStatus()
    }
    fun validateUsername(input: String): Boolean {
        val u = input.trim()

        val error = when {
            u.isEmpty() -> "Username cannot be empty."
            u.length !in 3..20 -> "Username must be between 3 and 20 characters."
            u.contains(" ") -> "Username cannot contain spaces."
            !Regex("^[A-Za-z0-9._]+$").matches(u) -> "Use only letters, numbers, '.' and '_'."
            u.startsWith(".") || u.startsWith("_") -> "Username cannot start with '.' or '_'."
            u.endsWith(".") || u.endsWith("_") -> "Username cannot end with '.' or '_'."
            u.contains("..") || u.contains("__") || u.contains("._") || u.contains("_.") ->
                "Avoid consecutive special characters."
            u.lowercase() in setOf("admin", "root", "support", "system", "null") ->
                "This username is not allowed."
            else -> null
        }

        _usernameError.value = error   // ✅ valid olanda null yazılır
        return error == null
    }

    fun registerauth(email: String, password: String, username:String) {
        viewModelScope.launch {
        if (!validateAll(email, password,username)) return@launch
            if (username.isEmpty()) {
                _authstate.value = Result.failure(IllegalArgumentException("The username is not empty."))
                return@launch
            }
      val result= withContext(Dispatchers.IO) {
          prepo.registerauth(email, password)

      }
            if (result.isSuccess){
                dataStoreManager.setUsername(username)
                dataStoreManager.setemail(email)
                dataStoreManager.setLoggedIn(true)

            }
            _authstate.value = result
    }}

    fun checkUserStatus() {
            _currentuser.postValue(prepo.getcurrentuser())
    }

    fun validateEmail(email: String){
        _emailError.value=when{
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                 "Please enter a valid email address"
                else->null

        }

    }
    fun validatePassword(password: String){
        _passwordError.value=when{
            password.isEmpty() -> null
            password.length < 8 -> "Password must be at least 8 characters long."
            !password.any { it.isUpperCase() } -> "Must contain at least one uppercase letter."
            !password.any { it.isLowerCase() } -> "Must contain at least one lowercase letter."
            !password.any { it.isDigit() } -> "Must contain at least one number."
            password.contains(" ") -> "Password cannot contain spaces"
            else -> null
        }
    }

     fun validateAll(email: String, password: String,username: String): Boolean {

         validateEmail(email)
         validatePassword(password)
         validateUsername(username)

        return when{
            _emailError.value!=null->false
            _passwordError.value!=null->false
            _usernameError.value!=null->false
            else-> true

        }


    }
}