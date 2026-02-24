package com.yaqubabbasov.bobofood.ui.profil

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilViewModel : ViewModel() {
     val firestorage = Firebase.storage.reference
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