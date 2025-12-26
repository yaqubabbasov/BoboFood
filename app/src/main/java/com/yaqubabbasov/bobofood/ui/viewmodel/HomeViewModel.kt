package com.yaqubabbasov.bobofood.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yaqubabbasov.bobofood.data.entity.Yemekler
import com.yaqubabbasov.bobofood.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(val prepo: ProductRepository) : ViewModel() {
    init {
        productdown()
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


}