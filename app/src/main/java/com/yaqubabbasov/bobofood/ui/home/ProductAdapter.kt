package com.yaqubabbasov.bobofood.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.databinding.ProductCardBinding

class ProductAdapter(
    var mcontext: Context,
    var productlist: List<Yemekler>,
    private val onItemClick: (Yemekler) -> Unit
) : RecyclerView.Adapter<ProductAdapter.Productviewholder>() {
    inner class Productviewholder(var binding: ProductCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): Productviewholder {
        val card = ProductCardBinding.inflate(LayoutInflater.from(mcontext), parent, false)
        return Productviewholder(card)
    }

    override fun onBindViewHolder(
        holder: Productviewholder,
        position: Int,
    ) {
        val t = productlist[position]
        holder.binding.cardprname.text = t.yemekAdi
        holder.binding.cardprice.text = "${t.yemekFiyat} \u20BA"
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${t.yemekResimAdi}"
        Glide.with(mcontext).load(url).override(150, 130).into(holder.binding.cardprimage)
        var lastClickTime = 0L
        holder.binding.cardView.setOnClickListener {
            val now = System.currentTimeMillis()
            if (now - lastClickTime > 500 && holder.binding.root.findNavController().currentDestination != null) {
                lastClickTime = now
                onItemClick(t)
            }
        }
    }

    override fun getItemCount(): Int {
        return productlist.size
    }

    fun updateList(newlist: List<Yemekler>) {
        productlist = newlist
        notifyDataSetChanged()
    }
}