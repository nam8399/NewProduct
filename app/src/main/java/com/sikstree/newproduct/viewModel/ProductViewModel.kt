package com.sikstree.newproduct.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sikstree.minecraftstatus.Model.Event
import com.sikstree.newproduct.Data.MyApplication
import com.sikstree.newproduct.Data.ProductData
import com.sikstree.newproduct.Data.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ProductViewModel(application: Application) : AndroidViewModel(application){


    private val title = "ProductViewModel"

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    var onclickIdx = MutableLiveData<Int>()
    var categoryIdx = MutableLiveData<Int>()
    var allData = MutableLiveData<ArrayList<ProductData>>()
    var riceData = MutableLiveData<ArrayList<ProductData>>()
    var cookieData = MutableLiveData<ArrayList<ProductData>>()
    var breadData = MutableLiveData<ArrayList<ProductData>>()
    var drinkData = MutableLiveData<ArrayList<ProductData>>()

    init {
        categoryIdx.value = 0 // 0 : 전체, 1 : 신선제품, 2 : 과자, 3 : 빵, 4 : 음료
        onclickIdx.value = 0 // 0 : 전체, 1 : 신선제품, 2 : 과자, 3 : 빵, 4 : 음료
        allData.value = ArrayList<ProductData>()
        riceData.value = ArrayList<ProductData>()
        cookieData.value = ArrayList<ProductData>()
        breadData.value = ArrayList<ProductData>()
        drinkData.value = ArrayList<ProductData>()
    }


    fun clickCategory(idx : Int) {
        onclickIdx.value = idx
    }


    fun selectItem(data : ArrayList<ProductData>, idx : Int): ArrayList<ProductData>? {
        if (data.size == 0) {
            return null
        }

        var itemData = arrayListOf<ProductData>()

        for (i in 0..data.size-1) {
            if (data.get(i).review_category_idx == idx) {
                itemData.add(data.get(i))
            }
        }

        return itemData
    }


}