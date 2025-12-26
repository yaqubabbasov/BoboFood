package com.yaqubabbasov.bobofood.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.databinding.ProductCardBinding
import com.yaqubabbasov.bobofood.ui.fragment.HomeFragmentDirections

class ProductAdapter(var mcontext: Context,var productlist:List<Yemekler>): RecyclerView.Adapter<ProductAdapter.Productviewholder>() {
    inner class Productviewholder(var binding: ProductCardBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): Productviewholder {
       val card= ProductCardBinding.inflate(LayoutInflater.from(mcontext),parent,false)
        return Productviewholder(card)
    }

    override fun onBindViewHolder(
        holder: Productviewholder,
        position: Int,
    ) {
        val t=productlist[position]
        holder.binding.cardprname.text=t.yemekAdi
        holder.binding.cardprice.text = "${t.yemekFiyat} \u20BA"
        val url="http://kasimadalan.pe.hu/yemekler/resimler/${t.yemekResimAdi}"
        Glide.with(mcontext).load(url).override(150,130).into(holder.binding.cardprimage)
        holder.binding.cardView.setOnClickListener {
            var bg= HomeFragmentDirections.homdetbridge(t)
            it.findNavController().navigate(bg)
        }

    }

    override fun getItemCount(): Int {
       return productlist.size
    }
    fun updateList(newlist:List<Yemekler>){
        productlist=newlist
        notifyDataSetChanged()

    }


}