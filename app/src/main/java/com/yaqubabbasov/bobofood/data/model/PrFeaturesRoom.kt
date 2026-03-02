package com.yaqubabbasov.bobofood.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("features")
data class PrFeaturesRoom(
    @PrimaryKey
    @ColumnInfo("product_id")
    val product_id: Int,
    @ColumnInfo("prfeatures")
    val features: String
)