package com.yaqubabbasov.bobofood.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.data.model.PrFeaturesRoom
import com.yaqubabbasov.bobofood.data.model.RoomFood

@Dao
interface ProductRoomDao {
    @Query("SELECT * FROM boboheart")
    suspend fun getAllfavourites(): List<RoomFood>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addfavourite(product:RoomFood)
    @Query("SELECT EXISTS(SELECT 1 FROM boboheart WHERE yemek_id = :id)")
    suspend fun isFavourite(id: Int): Boolean
    @Delete
    suspend fun removeFavourite(product: RoomFood)

}