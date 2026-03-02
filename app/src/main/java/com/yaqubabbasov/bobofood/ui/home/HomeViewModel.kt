package com.yaqubabbasov.bobofood.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaqubabbasov.bobofood.data.datasource.PrefsManager
import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val prepo: ProductRepository, val datastoremanager: PrefsManager) : ViewModel() {
    init {
        productdown()
    }
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username
    fun loadUsername() = viewModelScope.launch {
        _username.value = datastoremanager.getUsername()
    }

    val productlist= MutableLiveData<List<Yemekler>>()
    fun productdown(){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                productlist.value=prepo.productdown()
            }catch (e: Exception){

            }

        }
    }
    fun addtocart(food_name:String,
                  food_image_name:String,
                  food_price: Int,
                  food_order_quantity:Int
    )= viewModelScope.launch {
        val username = datastoremanager.getUsername()
        if (username.isBlank()) {
            Log.e("addtocart", "Username boşdur — register zamanı setUsername çağırılmayıb")
            return@launch
        }

        prepo.addtocart(food_name,
            food_image_name,
            food_price,
            food_order_quantity,
            username)
        Log.e("control","$food_name,$food_image_name,$food_price,$food_order_quantity,$username")

    }


}