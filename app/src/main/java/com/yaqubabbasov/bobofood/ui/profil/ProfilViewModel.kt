package com.yaqubabbasov.bobofood.ui.profil

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.yaqubabbasov.bobofood.data.datasource.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class ProfilViewModel @Inject constructor(private val datastore: PrefsManager) : ViewModel() {
    val firestorage = Firebase.storage.reference
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    fun loadUsername() = viewModelScope.launch {
        _username.value = datastore.getUsername()
        _email.value = datastore.getEmail()
    }
    fun uploadingimage(uri: Uri, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val filename = "images/${System.currentTimeMillis()}.jpg"
                val uploadtask = firestorage.child(filename).putFile(uri)
                uploadtask.addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener { exception->
                    onFailure(exception)

                }

            } catch (e: Exception) {
                onFailure(e)



            }


        }

    }
}