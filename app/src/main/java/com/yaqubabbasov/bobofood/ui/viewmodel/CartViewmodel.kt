package com.yaqubabbasov.bobofood.ui.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaqubabbasov.bobofood.data.entity.Basket_List
import com.yaqubabbasov.bobofood.data.entity.Food_Add_Delete
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
class CartViewmodel @Inject constructor(val prepo: ProductRepository): ViewModel() {
    init {
       getcatuser("jacob")
    }
    private val pendingUpdates = mutableMapOf<Int, Int>()
    private var updateJob: Job? = null
    var _basketlive= MutableLiveData< List<Basket_List>>()
    val basketLive: LiveData<List<Basket_List>> get() = _basketlive
    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> get() = _totalPrice
    fun getcatuser(user:String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = prepo.getcart(user) ?: emptyList()  // null safety əlavə olunur
                _basketlive.value = result
                    //setbasketitem(result)
            } catch (e: Exception) {
                Log.e("getcatuser", "Xəta: ${e.message}")
                _basketlive.value = emptyList()  // problem olsa belə boş list göndər
            }
        }
    }

    fun updateBasketItem(item: Basket_List) {
        CoroutineScope(Dispatchers.Main).launch {
            val list = _basketlive.value?.toMutableList() ?: mutableListOf()
            val index = list.indexOfFirst { it.sepet_yemek_id == item.sepet_yemek_id }
            if (index != -1) {
                list[index] = item
                _basketlive.value = list
                calculateTotal()
            }
            try {
                prepo.getdeleteAllFoods("jacob", item.yemek_adi)
                prepo.addtocart(
                    item.yemek_adi,
                   item.yemek_resim_adi,
                    item.yemek_fiyat,
                   item.yemek_siparis_adet,
                    "jacob",
                )

                // 3. Local LiveData-ni yenilə
            } catch (e: Exception) {
                Log.e("updateBasketItem", "Error: ${e.message}")
            }
        }
    }

    fun removeBasketItem(item: Basket_List) {
        val list = _basketlive.value?.toMutableList() ?: mutableListOf()
        list.removeAll { it.sepet_yemek_id == item.sepet_yemek_id }
        _basketlive.value = list
        calculateTotal()
    }
    fun calculateTotal() {
        _basketlive.value?.let { list ->
            _totalPrice.value = list.sumOf { it.yemek_fiyat * it.yemek_siparis_adet }
        }
    }

   /* fun getcartdown(username:String){
        CoroutineScope(Dispatchers.Main).launch {
            _basketlive.value=prepo.getcartdown(username)


        }
    }*/
   fun getdeleteAllFood(item: Basket_List, username: String){
       val list = _basketlive.value?.toMutableList() ?: mutableListOf()
       list.removeAll { it.sepet_yemek_id == item.sepet_yemek_id }
       _basketlive.value = list
       calculateTotal()

       // 2. Serverdə sil (fon iş)
       viewModelScope.launch(Dispatchers.IO) {
           try {
               prepo.getdeleteAllFoods(username, item.yemek_adi)
           } catch (e: Exception) {
               Log.e("removeBasketItem", "Server error: ${e.message}")
           }
       }
   }
    fun updateBasketItemLocally(item: Basket_List, newCount: Int) {
        val list = _basketlive.value?.toMutableList() ?: mutableListOf()
        val index = list.indexOfFirst { it.sepet_yemek_id == item.sepet_yemek_id }

        // 1. Lokal UI update
        if (index != -1) {
            if (newCount <= 0) {
                list.removeAt(index) // 1-dən aşağı olduqda sil
            } else {
                list[index] = list[index].copy(yemek_siparis_adet = newCount)
            }
            _basketlive.value = list.toList()
            calculateTotal()
        }

        // 2. Pending updates üçün map
        pendingUpdates[item.sepet_yemek_id] = newCount

        // 3. Coroutine server update
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            delay(500)
            val currentUpdates = pendingUpdates.toMap() // snapshot
            currentUpdates.forEach { (id, count) ->
                val itemToUpdate = _basketlive.value?.find { it.sepet_yemek_id == id }
                try {
                    if (count <= 0) {
                        prepo.getdeleteAllFoods("jacob", item.sepet_yemek_id.toString())
                    } else {
                        prepo.getdeleteAllFoods("jacob", itemToUpdate?.yemek_adi ?: "")
                        delay(100)
                        itemToUpdate?.let {
                            prepo.addtocart(
                                it.yemek_adi,
                                it.yemek_resim_adi,
                                it.yemek_fiyat,
                                count,
                                "jacob"
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("updateBasketItem", "Server error: ${e.message}")
                }
            }
            pendingUpdates.clear()
            // 4. Server refreshdən qaç, UI-də local state artıq silinmiş məhsulu saxlayır
            // calculateTotal() lazım olduqda çağır
            calculateTotal()

        }
    }
    /*fun updateBasketItemWithServerSync(item: Basket_List, newCount: Int) {
        // 2. Server update (debounce)
        pendingUpdates[item.sepet_yemek_id] = newCount
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            delay(500) // 0.5 saniyə gözlə, sonra server update
            pendingUpdates.forEach { (id, count) ->
                val itemToUpdate = list.find { it.sepet_yemek_id == id } ?: return@forEach
                try {
                    prepo.getdeleteAllFoods("jacob", itemToUpdate.yemek_adi)
                    prepo.addtocart(
                        itemToUpdate.yemek_adi,
                        itemToUpdate.yemek_resim_adi,
                        itemToUpdate.yemek_fiyat,
                        count,
                        "jacob"
                    )
                } catch (e: Exception) {
                    Log.e("updateBasketItem", "Server error: ${e.message}")
                }
            }
            pendingUpdates.clear()
        }*/
    }

