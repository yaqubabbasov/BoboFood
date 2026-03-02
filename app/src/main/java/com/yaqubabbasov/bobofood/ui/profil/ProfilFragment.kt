package com.yaqubabbasov.bobofood.ui.profil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.yaqubabbasov.bobofood.databinding.FragmentProfilBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
@AndroidEntryPoint
class ProfilFragment : Fragment() {
        private  var _binding: FragmentProfilBinding?=null
        private val binding get() = _binding!!
        private val viewmodel: ProfilViewModel by viewModels()


        private val PREFS_NAME = "my_prefs"
        private val KEY_IMAGE_URI = "image_uri"
        private val pickImage = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri ?: return@registerForActivityResult


            try {
                requireContext().contentResolver.takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) { }

            saveImageUri(uri)
            Glide.with(this).load(uri).into(binding.uploadimage)
        }

        private fun saveImageUri(uri: Uri) {
            requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putString(KEY_IMAGE_URI, uri.toString()).apply()
        }
        private fun loadSavedImage() {
            val uriStr = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_IMAGE_URI, null)

            if (!uriStr.isNullOrBlank()) {
                Glide.with(this).load(Uri.parse(uriStr)).into(binding.uploadimage)
            }
        }



        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {
            _binding= FragmentProfilBinding.inflate(inflater,container,false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            loadSavedImage()
                binding.cardprofilimage.setOnClickListener {
                    pickImage.launch(arrayOf("image/*"))
                }

                darkmode()

                viewmodel.username.observe(viewLifecycleOwner) { u ->
                    val formatted = u.trim().lowercase().replaceFirstChar { it.titlecase() }
                    binding.nametext.text = if (formatted.isBlank()) "Guest" else formatted
                }
                viewmodel.email.observe(viewLifecycleOwner) { email ->
                    binding.emailtext.text = email
                }

                viewmodel.loadUsername()


        }
        private fun darkmode(){
            val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
            val isDarkMode = prefs.getBoolean("dark_mode", false)


            binding.switch1.isChecked = isDarkMode


            AppCompatDelegate.setDefaultNightMode(
                if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )


            binding.switch1.setOnCheckedChangeListener { _, isChecked ->
                prefs.edit().putBoolean("dark_mode", isChecked).apply()


                AppCompatDelegate.setDefaultNightMode(
                    if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }







        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    }