package com.yaqubabbasov.bobofood.data.entity

import com.google.gson.annotations.SerializedName

data class Basket_Answer(
    //@SerializedName("sepet_yemekler")
    val sepet_yemekler: List<Basket_List>,
    //@SerializedName("success")
    val success: Int) {
}