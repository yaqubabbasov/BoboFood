package com.yaqubabbasov.bobofood.data.entity


import com.google.gson.annotations.SerializedName

data class ProductAnswer(
    @SerializedName("success")
    val success: Int?,
    @SerializedName("yemekler")
    val yemekler: List<Yemekler>
)