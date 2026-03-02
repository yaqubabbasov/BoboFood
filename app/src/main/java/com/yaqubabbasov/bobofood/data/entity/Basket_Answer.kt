package com.yaqubabbasov.bobofood.data.entity

import com.google.gson.annotations.SerializedName

data class Basket_Answer(
    val sepet_yemekler: List<Basket_List>,
    val success: Int) {
}