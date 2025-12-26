package com.yaqubabbasov.bobofood.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boboheart")
data class RoomFood(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "yemek_id")
    val yemek_id: Int,
    @ColumnInfo(name = "yemek_adi")
    val yemek_adi: String,
    @ColumnInfo(name = "yemek_resim_adi")
    val yemek_resim_adi: String,
    @ColumnInfo(name = "yemek_fiyat")
    val yemek_fiyat: String
)