package com.yaqubabbasov.bobofood.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaqubabbasov.bobofood.data.datasource.PrefsManager
import com.yaqubabbasov.bobofood.data.model.RoomFood
import com.yaqubabbasov.bobofood.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(val prepo: ProductRepository, val datastoremanager: PrefsManager): ViewModel() {
    val count= MutableLiveData("1")
    val username= MutableLiveData<String>()
    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> get() = _isFavourite

    fun increment() {
        count.value = (count.value!!.toInt() + 1).toString()
    }

    fun decrement() {
        val current = count.value!!.toInt()
        if (current > 1) {
            count.value = (current - 1).toString()
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



    fun checkFavourite(yemekId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            _isFavourite.value = prepo.isFavourite(yemekId)
        }
    }


    fun toggleFavourite(product: RoomFood) {
      CoroutineScope(Dispatchers.Main).launch {
            if (prepo.isFavourite(product.yemek_id)) {
                val delete =prepo.removeFavourite(product)
                Log.e("remove","$delete")
                _isFavourite.value = false

            } else {
                prepo.addfavouritess(product)
                _isFavourite.value = true
            }

        }
    }
    private val _featureText = MutableLiveData<String>()
    val featureText: LiveData<String> = _featureText

    fun loadFeature(yemekId: String) = viewModelScope.launch {
        _featureText.value = prepo.getFeatureTextForProduct(yemekId) ?: "Xüsusiyyət yoxdur"
    }




}