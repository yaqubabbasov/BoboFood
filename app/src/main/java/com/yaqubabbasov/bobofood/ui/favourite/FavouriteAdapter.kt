package com.yaqubabbasov.bobofood.ui.favourite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yaqubabbasov.bobofood.data.model.RoomFood
import com.yaqubabbasov.bobofood.databinding.FavouriteCardBinding

class FavouriteAdapter(var context: Context, var favourtielist: List<RoomFood>, var viewmodel: FavouriteViewModel): RecyclerView.Adapter<FavouriteAdapter.FavourtieViewHolder>() {
     class FavourtieViewHolder(val binding: FavouriteCardBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FavourtieViewHolder {
       val binding= FavouriteCardBinding.inflate(LayoutInflater.from(context), parent,false)
        return FavourtieViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FavourtieViewHolder,
        position: Int,
    ) {
       val item= favourtielist[position]
        holder.binding.basketName.text=item.yemek_adi
        holder.binding.basketcost.text="${item.yemek_fiyat} \u20BA"
        val url="http://kasimadalan.pe.hu/yemekler/resimler/${item.yemek_resim_adi}"
        Glide.with(context).load(url).override(99,81).into(holder.binding.basketImage)
        holder.binding.deletebutton.setOnClickListener {
            viewmodel.removefavourite(item)
        }
    }

    override fun getItemCount(): Int {
     return favourtielist.size
    }
    fun updatelist(list: List<RoomFood>){
        favourtielist=list
        notifyDataSetChanged()
    }

}