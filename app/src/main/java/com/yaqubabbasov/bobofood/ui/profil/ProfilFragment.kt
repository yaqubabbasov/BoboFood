package com.yaqubabbasov.bobofood.ui.profil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yaqubabbasov.bobofood.databinding.FragmentProfilBinding

class ProfilFragment : Fragment() {
        private  var _binding: FragmentProfilBinding?=null
        private val binding get() = _binding!!
        private val viewmodel: ProfilViewModel by viewModels()
        private var selectedIMgeUrl: Uri?=null


        private lateinit var imageView: ImageView
        var REQUEST_CODE_PICK_IMAGE = 1
        private val PREFS_NAME = "my_prefs"
        private val KEY_IMAGE_URI = "image_uri"
        private val pickimagelauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ){ uri : Uri?->
            uri?.let{

                binding.profilphoto.setImageURI(it)
                selectedIMgeUrl=it
                viewmodel.uploadingimage(it,
                    onSuccess = {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    })
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
            binding.cardprofilimage.setOnClickListener {
             openGallery()
            }
            darkmode()

        }
        private fun darkmode(){
            val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
            val isDarkMode = prefs.getBoolean("dark_mode", false)

            // Switch-in vəziyyətini göstəririk
            binding.switch1.isChecked = isDarkMode

            // Hal-hazırki dark mode-u tətbiq et
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )

            // Listener
            binding.switch1.setOnCheckedChangeListener { _, isChecked ->
                prefs.edit().putBoolean("dark_mode", isChecked).apply()

                // Dark mode-u dəyiş, activity-ni recreate et
                AppCompatDelegate.setDefaultNightMode(
                    if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }

        private fun openGallery() {
            val intent = Intent(
                Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
                data?.data?.let { uri ->
                    try {
                        requireContext().contentResolver.takePersistableUriPermission(
                            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        showImageFromUri(uri)

                    }catch (e: Exception){
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Şəkil seçərkən xəta baş verdi", Toast.LENGTH_SHORT).show()
                    }





                }
            }
        }

        private fun showImageFromUri(uri: Uri) {
            try {
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    val options = BitmapFactory.Options().apply {
                        inSampleSize = 4
                    }
                    val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
                    binding.uploadimage.setImageBitmap(bitmap)
                    inputStream.close()
                } else {
                    Toast.makeText(requireContext(), "Cannot open image", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }


    }