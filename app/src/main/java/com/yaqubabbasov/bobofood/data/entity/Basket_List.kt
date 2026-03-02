package com.yaqubabbasov.bobofood.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Basket_List(
    var sepet_yemek_id: Int,
    var yemek_adi:String,
    var yemek_resim_adi: String,
    var yemek_fiyat: Int,
    var yemek_siparis_adet: Int,
    var kullanici_adi: String
): Parcelable