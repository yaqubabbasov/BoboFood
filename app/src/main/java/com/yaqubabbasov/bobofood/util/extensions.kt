package com.yaqubabbasov.bobofood.util

import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.data.model.RoomFood

fun Yemekler.toRoomFood(): RoomFood {
    return RoomFood(
        yemek_id = this.yemekId.toInt(),
        yemek_adi = this.yemekAdi,
        yemek_fiyat = this.yemekFiyat,
        yemek_resim_adi = this.yemekResimAdi
    )
}