package com.yaqubabbasov.bobofood.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.data.model.PrFeaturesRoom
import com.yaqubabbasov.bobofood.data.model.RoomFood

@Database(entities = [RoomFood::class, PrFeaturesRoom::class], version = 2,
    exportSchema = false
)
abstract class DataBase: RoomDatabase() {
    abstract fun getdao(): ProductRoomDao
    abstract fun featureDao(): FeatureDao

}