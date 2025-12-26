package com.yaqubabbasov.bobofood.data.entity


import com.google.gson.annotations.SerializedName

data class Food_Add_Delete(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int
)