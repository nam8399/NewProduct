package com.sikstree.newproducts.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChoiceViewModel() : ViewModel() {
    private val title = "ReviewViewModel"

    var onclickIdx = MutableLiveData<Int>()
    var btnEnable = MutableLiveData<Boolean>()


    init {
        onclickIdx.value = 0 // 0 : 전체, 1 : 신선제품, 2 : 과자, 3 : 빵, 4 : 음료
        btnEnable.value = false
    }


    fun clickIcon(idx : Int) {
        onclickIdx.value = idx
    }





}