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

    var categoryIdx = MutableLiveData<Int>()

    init {
        categoryIdx.value = 0 // 0 : 전체, 1 : 신선제품, 2 : 과자, 3 : 빵, 4 : 음료
    }





}