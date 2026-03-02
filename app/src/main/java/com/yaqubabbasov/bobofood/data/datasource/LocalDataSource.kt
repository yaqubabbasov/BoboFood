package com.yaqubabbasov.bobofood.data.datasource

import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.data.model.PrFeaturesRoom
import com.yaqubabbasov.bobofood.data.model.RoomFood
import com.yaqubabbasov.bobofood.retrofit.ProductDao
import com.yaqubabbasov.bobofood.room.FeatureDao
import com.yaqubabbasov.bobofood.room.ProductRoomDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource (val roomdao: ProductRoomDao,
                       val featureDao: FeatureDao
){
    suspend fun addfavouritess(list: RoomFood){
        withContext(Dispatchers.IO){
            roomdao.addfavourite(list)
        }
    }
    suspend fun getAllfavourites(): List<RoomFood> =
        withContext(Dispatchers.IO){
            return@withContext roomdao.getAllfavourites()
        }
    suspend fun isFavourite(id: Int): Boolean =
        withContext(Dispatchers.IO){
            return@withContext roomdao.isFavourite(id)
        }
    suspend fun removeFavourite(product: RoomFood){
        withContext(Dispatchers.IO){
            roomdao.removeFavourite(product)
        }
    }

    suspend fun getFeatureById(id: Int): PrFeaturesRoom? = featureDao.getById(id)
}