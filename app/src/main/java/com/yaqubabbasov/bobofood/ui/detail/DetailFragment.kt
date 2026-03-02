package com.yaqubabbasov.bobofood.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.data.model.RoomFood
import com.yaqubabbasov.bobofood.databinding.FragmentDetailBinding

import com.yaqubabbasov.bobofood.ui.favourite.FavouriteViewModel
import com.yaqubabbasov.bobofood.util.toRoomFood
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
        private lateinit var binding: FragmentDetailBinding
        private val viewmodel: DetailViewModel by viewModels()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
            binding.lifecycleOwner=viewLifecycleOwner
            val bundle: DetailFragmentArgs by navArgs()
            val dt= bundle.foodcl
            binding.dtlFragment=this
            binding.foodcls=dt
            val url="http://kasimadalan.pe.hu/yemekler/resimler/${dt.yemekResimAdi}"
            Glide.with(this).load(url).override(350,200).into(binding.detailimage)

            viewmodel.count.observe(viewLifecycleOwner){
                binding.tcount=it

            }

            binding.plusbutton.setOnClickListener {
                viewmodel.increment()
            }
            binding.minusbutton.setOnClickListener {
                viewmodel.decrement()
            }


            viewmodel.checkFavourite(dt.yemekId.toInt())

            binding.addtocartbutton.setOnClickListener {
                val currentCount = viewmodel.count.value!!.toInt()
                viewmodel.addtocart(dt.yemekAdi,dt.yemekResimAdi,dt.yemekFiyat.toInt(),currentCount)
                findNavController().navigate(R.id.dtlcart)
         }




            viewmodel.isFavourite.observe(viewLifecycleOwner) { isFav ->
                val colorRes = if (isFav) R.color.red else R.color.light_gray
                binding.heartbutton.setIconTintResource(colorRes)
            }

            binding.heartbutton.setOnClickListener {
                viewmodel.toggleFavourite(dt.toRoomFood())
            }
            binding.backmaterial.setOnClickListener {
                findNavController().popBackStack()
            }
            viewmodel.featureText.observe(viewLifecycleOwner) { text ->
                binding.featuretext.text = text
            }

            viewmodel.loadFeature(dt.yemekId)



            return binding.root
        }



}