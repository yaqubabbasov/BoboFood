package com.yaqubabbasov.bobofood.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.data.model.RoomFood

@Database(entities = [RoomFood::class], version = 1)
abstract class DataBase: RoomDatabase() {
    abstract fun getdao(): ProductRoomDao

}