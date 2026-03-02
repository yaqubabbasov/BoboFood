package com.yaqubabbasov.bobofood.data.repository

import com.yaqubabbasov.bobofood.data.datasource.LocalDataSource
import com.yaqubabbasov.bobofood.data.datasource.ProductDataSource
import com.yaqubabbasov.bobofood.data.entity.Basket_List
import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.data.model.RoomFood
import com.yaqubabbasov.bobofood.room.ProductRoomDao

class ProductRepository(var pds: ProductDataSource,
                        var roomdao: LocalDataSource
) {
    suspend fun registerauth(email:String,password: String): Result<String> =pds.registerauth(email,password)
     fun getcurrentuser():Result<String> =pds.getCurrentUser()
    suspend fun productdown():List<Yemekler>{
        return pds.productdown()

    }
    suspend fun addtocart(food_name:String,
                          food_image_name:String,
                          food_price: Int,
                          food_order_quantity:Int,
                          username: String)= pds.addtocart(food_name,
        food_image_name,
        food_price,
        food_order_quantity,
        username)
    suspend fun getcart(username: String): List<Basket_List> = pds.getcartdown(username)

    suspend fun getdeleteAllFoods( username: String,foodname: String,)=pds.deleteAllFoods(username,foodname)
    suspend fun addfavouritess(list: RoomFood)=roomdao.addfavouritess(list)
    suspend fun getAllfavourites(): List<RoomFood> =roomdao.getAllfavourites()
    suspend fun isFavourite(id: Int): Boolean {
        return roomdao.isFavourite(id)
    }
    suspend fun removeFavourite(product: RoomFood)=roomdao.removeFavourite(product)

    suspend fun getFeatureTextForProduct(yemekId: String): String? {
        val id = yemekId.toIntOrNull() ?: return null
        return roomdao.getFeatureById(id)?.features
    }






}