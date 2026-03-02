package com.yaqubabbasov.bobofood.ui.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.integrity.internal.u
import com.yaqubabbasov.bobofood.data.datasource.PrefsManager
import com.yaqubabbasov.bobofood.data.entity.Basket_List
import com.yaqubabbasov.bobofood.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewmodel @Inject constructor(val prepo: ProductRepository, datastore: PrefsManager): ViewModel() {
    private var currentUser: String = ""
    init {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser = datastore.getUsername()
            if (currentUser.isBlank()) return@launch
            getcatuser(currentUser)
        }
    }
    fun refreshCart() {
        if (currentUser.isBlank()) return
        getcatuser(currentUser)
    }
    private val pendingUpdates = mutableMapOf<Int, Pair<String, Int>>()
    private var updateJob: Job? = null
    var _basketlive= MutableLiveData<List<Basket_List>>()
    val basketLive: LiveData<List<Basket_List>> get() = _basketlive
    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> get() = _totalPrice
    fun getcatuser(user:String){
        viewModelScope.launch {
            try {
                val result = prepo.getcart(user) ?: emptyList()
                _basketlive.value = result
            } catch (e: Exception) {
                Log.e("getcatuser", "Error: ${e.message}")
                _basketlive.value = emptyList()
            }
        }
    }




    fun calculateTotal() {
        _basketlive.value?.let { list ->
            _totalPrice.value = list.sumOf { it.yemek_fiyat * it.yemek_siparis_adet }
        }
    }


    fun getdeleteAllFood(item: Basket_List){
        val list = _basketlive.value?.toMutableList() ?: mutableListOf()
        list.removeAll { it.sepet_yemek_id == item.sepet_yemek_id }
        _basketlive.value = list
        calculateTotal()


        viewModelScope.launch(Dispatchers.IO) {
            try {
                prepo.getdeleteAllFoods(currentUser, item.yemek_adi)
            } catch (e: Exception) {
                Log.e("removeBasketItem", "Server error: ${e.message}")
            }
        }
    }
    fun updateBasketItemLocally(item: Basket_List, newCount: Int) {
        // 1) UI update
        val list = _basketlive.value?.toMutableList() ?: mutableListOf()
        val index = list.indexOfFirst { it.sepet_yemek_id == item.sepet_yemek_id }

        if (index != -1) {
            if (newCount <= 0) list.removeAt(index)
            else list[index] = list[index].copy(yemek_siparis_adet = newCount)

            _basketlive.value = list
            calculateTotal()
        }

        // 2) pending update-də item məlumatını saxla (silinsə də)
        pendingUpdates[item.sepet_yemek_id] = item.yemek_adi to newCount

        // 3) server update
        updateJob?.cancel()
        updateJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)

            val snapshot = pendingUpdates.toMap()
            pendingUpdates.clear()

            snapshot.forEach { (_, pair) ->
                val (name, count) = pair
                try {
                    // həmişə əvvəl sil
                    prepo.getdeleteAllFoods(currentUser, name)

                    if (count > 0) {
                        delay(100)
                        // count > 0 isə yenidən əlavə et (server modelin belədirsə)
                        // burada item-in digər field-ləri də lazımdırsa, onları da pendingUpdates-də saxla
                    }
                } catch (e: Exception) {
                    Log.e("updateBasketItem", "Server error: ${e.message}", e)
                }
            }

            withContext(Dispatchers.Main) { calculateTotal() }
        }
    }

}