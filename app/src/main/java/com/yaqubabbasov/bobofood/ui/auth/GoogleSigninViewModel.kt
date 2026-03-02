package com.yaqubabbasov.bobofood.ui.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.data.datasource.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
@HiltViewModel
class GoogleSigninViewModel @Inject constructor(val prefsManager: PrefsManager) : ViewModel(){
        private var auth: FirebaseAuth = FirebaseAuth.getInstance()
        val authstate: LiveData<Result<String?>> get() = _authstate

        private val _authstate = MutableLiveData<Result<String?>>()


        fun getGoogleSignInClient(context: Context): GoogleSignInClient {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.web_client_id))
                .requestEmail()
                .build()
            return GoogleSignIn.getClient(context, gso)

        }

    fun firebaseAuthwithGoogle(account: GoogleSignInAccount){
        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).await()
                val email = account.email.orEmpty()
                val name = account.displayName.orEmpty()
                val given = account.givenName.orEmpty()

                val username = when {
                    name.isNotBlank() -> name
                    given.isNotBlank() -> given
                    email.contains("@") -> email.substringBefore("@")
                    else -> "guest"
                }.trim()

                prefsManager.setLoggedIn(true)
                prefsManager.setemail(email)
                prefsManager.setUsername(username)

                _authstate.postValue(Result.success("Signed in with Google."))
            } catch (e: Exception){
                _authstate.postValue(Result.failure(e))
            }
        }
    }
        private suspend fun saveSessionFromFirebaseUser() {0
            val user = auth.currentUser
            val email = user?.email.orEmpty()
            val displayName = user?.displayName.orEmpty()

            val username = when {
                displayName.isNotBlank() -> displayName
                email.contains("@") -> email.substringBefore("@")
                else -> "guest"
            }.trim()

            prefsManager.setLoggedIn(true)
            prefsManager.setemail(email)
            prefsManager.setUsername(username)
        }


}