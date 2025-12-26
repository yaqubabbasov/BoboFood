package com.yaqubabbasov.bobofood.retrofit

class ApiUtil {

    companion object{
        val base_url="http://kasimadalan.pe.hu/"
        fun getProductDao(): ProductDao{
            return RetrofitClient.getclient(base_url).create(ProductDao::class.java)
        }
    }
}