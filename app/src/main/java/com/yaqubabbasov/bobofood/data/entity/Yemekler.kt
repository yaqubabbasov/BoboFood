package com.yaqubabbasov.bobofood.data.entity


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Parcelize
data class Yemekler(
    @SerializedName("yemek_adi")
    val yemekAdi: String,
    @SerializedName("yemek_id")
    val yemekId: String,
    @SerializedName("yemek_fiyat")
    val yemekFiyat: String,
    @SerializedName("yemek_resim_adi")
    val yemekResimAdi: String
): Parcelable