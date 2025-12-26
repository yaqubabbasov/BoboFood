package com.yaqubabbasov.bobofood.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.yaqubabbasov.bobofood.R
import com.yaqubabbasov.bobofood.data.model.RoomFood
import com.yaqubabbasov.bobofood.databinding.FragmentDetailBinding
import com.yaqubabbasov.bobofood.databinding.FragmentLoginfragmentBinding
import com.yaqubabbasov.bobofood.ui.viewmodel.DetailViewModel
import com.yaqubabbasov.bobofood.ui.viewmodel.FavouriteViewModel
import com.yaqubabbasov.bobofood.util.toRoomFood
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val viewmodel: DetailViewModel by viewModels()
    private val favouriteViewModel: FavouriteViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail, container, false)

        val bundle: DetailFragmentArgs by navArgs()
        val dt= bundle.foodcl
        binding.dtlFragment=this
        binding.foodcls=dt
        val url="http://kasimadalan.pe.hu/yemekler/resimler/${dt.yemekResimAdi}"
        Glide.with(this).load(url).override(350,200).into(binding.detailimage)

        viewmodel.count.observe(viewLifecycleOwner){
            binding.tcount=it

        }
        val user= "jacob"
        viewmodel.username.value=user

        binding.plusbutton.setOnClickListener {
            viewmodel.increment()
        }
        binding.minusbutton.setOnClickListener {
            viewmodel.decrement()
        }


        viewmodel.checkFavourite(dt.yemekId.toInt())
        val username = viewmodel.username.value!!
        binding.addtocartbutton.setOnClickListener {
            val currentCount = viewmodel.count.value!!.toInt()
            viewmodel.addtocart(dt.yemekAdi,dt.yemekResimAdi,dt.yemekFiyat.toInt(),currentCount,username)
            findNavController().navigate(R.id.dtlcart)
     }
        val cl= RoomFood(dt.yemekId.toInt(),dt.yemekAdi,dt.yemekResimAdi,dt.yemekFiyat)



        viewmodel.isFavourite.observe(viewLifecycleOwner) { isFav ->
            val colorRes = if (isFav) R.color.red else R.color.softgray
            binding.heartbutton.setIconTintResource(colorRes)
        }

        // Kliklə toggle et
        binding.heartbutton.setOnClickListener {
            viewmodel.toggleFavourite(dt.toRoomFood())
        }
        binding.backmaterial.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }



        return binding.root
    }


    /* fun addtocart(food_name:String,
                  food_image_name:String,
                  food_price: Int,
                  food_order_quantity:Int,
                  username: String){

        viewmodel.addtocart(food_name,food_image_name,food_price,food_order_quantity,username)
         Log.e("control","$food_name,$food_image_name,$food_price,$food_order_quantity,$username")
    }*/


}
