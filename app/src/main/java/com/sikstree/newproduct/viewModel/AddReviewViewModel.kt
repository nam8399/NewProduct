package com.sikstree.newproduct.viewModel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sikstree.newproduct.Data.IconData
import com.sikstree.newproduct.R

class AddReviewViewModel() : ViewModel() {
    private val title = "AddReviewViewModel"

    var onclickIdx = MutableLiveData<Int>()
    var btnEnable = MutableLiveData<Boolean>()


    init {
        onclickIdx.value = 0 // 0 : 전체, 1 : 신선제품, 2 : 과자, 3 : 빵, 4 : 음료
        btnEnable.value = false
    }







}