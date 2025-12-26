package com.yaqubabbasov.bobofood.data.datasource

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.yaqubabbasov.bobofood.data.entity.Basket_List
import com.yaqubabbasov.bobofood.data.entity.Food_Add_Delete
import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.retrofit.ProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProductDataSource(val pdao: ProductDao) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    suspend fun registerauth(email:String,password: String): Result<String>{
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(auth.currentUser?.email ?: "Success")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun getCurrentUser(): Result<String> {
        val email = auth.currentUser?.email
        return if (email != null) {
            Result.success(email)
        } else {
            Result.success("No Email")
        }
    }
suspend fun productdown():List<Yemekler> =
        withContext(Dispatchers.IO) {
            return@withContext pdao.productdown().yemekler

        }
    suspend fun addtocart(food_name:String,
                  food_image_name:String,
                  food_price: Int,
                  food_order_quantity:Int,
                  username: String) =
        withContext(Dispatchers.IO){
            pdao.addtocart(food_name,food_image_name,food_price,food_order_quantity,username)
            Log.e("control","$food_name,$food_image_name,$food_price,$food_order_quantity,$username")


    }
  suspend fun getcartdown(username:String):List<Basket_List> =
        withContext(Dispatchers.IO) {
            val cart= pdao.getcartdown(username).sepet_yemekler
            return@withContext mergeCart(cart)
        }
    private fun mergeCart(list: List<Basket_List>): List<Basket_List> {
        return list.groupBy { it.yemek_adi }
            .map { (_, items) ->
                val totalCount = items.sumOf { it.yemek_siparis_adet }
                val firstItem = items.first()
                firstItem.yemek_siparis_adet = totalCount
                firstItem
            }
    }
    suspend fun deleteAllFoods(username: String,foodname: String){
        withContext(Dispatchers.IO){
            try {
                val cart= pdao.getcartdown(username).sepet_yemekler
                val samefoods= cart.filter { it.yemek_adi==foodname }
                samefoods.forEach{item->
                    pdao.getdelete(item.sepet_yemek_id,username)
                }
            }catch (e: Exception){}

        }

    }



}