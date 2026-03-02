package com.yaqubabbasov.bobofood.retrofit

import com.yaqubabbasov.bobofood.data.entity.Basket_Answer
import com.yaqubabbasov.bobofood.data.entity.Basket_List
import com.yaqubabbasov.bobofood.data.entity.Food_Add_Delete
import com.yaqubabbasov.bobofood.data.entity.ProductAnswer
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ProductDao {
    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun productdown(): ProductAnswer
    @POST("yemekler/sepeteYemekEkle.php")
    @FormUrlEncoded
    suspend fun addtocart(@Field("yemek_adi") food_name:String,
                          @Field("yemek_resim_adi") food_image_name: String,
                          @Field("yemek_fiyat") food_price: Int,
                          @Field("yemek_siparis_adet") food_order_quantity: Int,
                          @Field("kullanici_adi") food_username: String): Food_Add_Delete
    @POST("yemekler/sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun getcartdown(@Field("kullanici_adi") username: String): Basket_Answer

    @POST("yemekler/sepettenYemekSil.php")
    @FormUrlEncoded
    suspend fun getdelete(@Field("sepet_yemek_id") food_id: Int,
                          @Field("kullanici_adi") username: String): Food_Add_Delete


}