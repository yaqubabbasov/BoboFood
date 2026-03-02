package com.yaqubabbasov.bobofood.ui.home

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.databinding.ProductCardBinding
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class ProductAdapter(
    private val activity: androidx.fragment.app.FragmentActivity,
    var mcontext: Context,
    var productlist: List<Yemekler>,
    val viewModel: HomeViewModel,
    private val onItemClick: (Yemekler) -> Unit
) : RecyclerView.Adapter<ProductAdapter.Productviewholder>() {
    class Productviewholder(var binding: ProductCardBinding) : RecyclerView.ViewHolder(binding.root)

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


        holder.binding.addbutton.setOnClickListener {
            viewModel.addtocart(t.yemekAdi, t.yemekResimAdi, t.yemekFiyat.toInt(), 1)


            MotionToast.createColorToast(
                activity,
                "Succesfull",
                "${t.yemekAdi} add to cart",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null
            )
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