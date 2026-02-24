package com.yaqubabbasov.bobofood.ui.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.yaqubabbasov.bobofood.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleSigninViewModel (): ViewModel(){
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val authstate: LiveData<Result<String?>> get() = _authstate
    private val _authstate = MutableLiveData<Result<String?>>()
    fun getGoogleSignİnclient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)

    }
    fun firebaseAuthwithGoogle(account: GoogleSignInAccount){
        CoroutineScope(Dispatchers.Main).launch {
            try {
             val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).await()
                _authstate.postValue(Result.success("Google ile oturum acildi"))

            }catch (e: Exception){
                _authstate.postValue(Result.failure(e))

            }
        }

    }


}