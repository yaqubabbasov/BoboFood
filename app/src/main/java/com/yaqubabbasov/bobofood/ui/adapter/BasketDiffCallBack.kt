package com.yaqubabbasov.bobofood.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.yaqubabbasov.bobofood.data.entity.Basket_List

class BasketDiffCallBack(private val oldList: List<Basket_List>,
                         private val newList: List<Basket_List>): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].sepet_yemek_id == newList[newItemPosition].sepet_yemek_id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}