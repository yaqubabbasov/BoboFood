package com.yaqubabbasov.bobofood.room

import androidx.room.Dao
import androidx.room.Query
import com.yaqubabbasov.bobofood.data.model.PrFeaturesRoom

@Dao
interface FeatureDao {
    @Query("SELECT * FROM features WHERE product_id = :id LIMIT 1")
    suspend fun getById(id: Int): PrFeaturesRoom?
}