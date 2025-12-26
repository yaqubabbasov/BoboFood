package com.yaqubabbasov.bobofood.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yaqubabbasov.bobofood.data.entity.Basket_List
import com.yaqubabbasov.bobofood.databinding.BasketCardBinding
import com.yaqubabbasov.bobofood.ui.viewmodel.CartViewmodel

class BasketAdapter(val mcontext: Context, var foodlist: List<Basket_List>,val viewmodel: CartViewmodel): RecyclerView.Adapter<BasketAdapter.BasketViewHolder>(){
    inner class BasketViewHolder(var binding: BasketCardBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BasketViewHolder {
       val card= BasketCardBinding.inflate(LayoutInflater.from(mcontext),parent,false)
        return BasketViewHolder(card)
    }

    override fun onBindViewHolder(
        holder: BasketViewHolder,
        position: Int,
    ) {
        //is this problem?
       // val t= foodlist.getOrNull(position) ?: return
        val t= foodlist[position]
        holder.binding.basketName.text=t.yemek_adi
        holder.binding.basketcost.text="${t.yemek_fiyat} \u20BA"
        val url="http://kasimadalan.pe.hu/yemekler/resimler/${t.yemek_resim_adi}"
        Glide.with(mcontext).load(url).override(99,81).into(holder.binding.basketImage)
        holder.binding.cartcount.text=t.yemek_siparis_adet.toString()
        holder.binding.cartplusbutton.setOnClickListener {
            val c= ++t.yemek_siparis_adet
            viewmodel.updateBasketItemLocally(t, c)
            holder.binding.cartcount.text=c.toString()

           // viewmodel.setbasketitem(foodlist.toList())
        }
        holder.binding.cartminusbutton.setOnClickListener {
         if (t.yemek_siparis_adet==1){
             viewmodel.getdeleteAllFood(t,"jacob")
         }else{
             val c= --t.yemek_siparis_adet
             viewmodel.updateBasketItemLocally(t, c)             //viewmodel.setbasketitem(foodlist.toList())
             holder.binding.cartcount.text=c.toString()
         }
        }
    }

    override fun getItemCount(): Int {
       return foodlist.size
    }
    fun updatelist(newItems:List<Basket_List>){
        val diffCallback = BasketDiffCallBack(foodlist, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        foodlist = newItems
        diffResult.dispatchUpdatesTo(this)
    }




}