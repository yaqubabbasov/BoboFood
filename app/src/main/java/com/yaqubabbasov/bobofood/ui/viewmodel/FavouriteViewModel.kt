package com.yaqubabbasov.bobofood.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaqubabbasov.bobofood.data.model.RoomFood
import com.yaqubabbasov.bobofood.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(val prepo: ProductRepository): ViewModel() {
    val mutablegfoodlist= MutableLiveData<List<RoomFood>>()
    init {
        getAllFavourites()
    }

    fun getAllFavourites(){
        CoroutineScope(Dispatchers.Main).launch {
            mutablegfoodlist.value=prepo.getAllfavourites()
        }
    }


}