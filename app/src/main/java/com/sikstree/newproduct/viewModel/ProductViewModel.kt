package com.sikstree.newproduct.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sikstree.minecraftstatus.Model.Event
import com.sikstree.newproduct.Data.HomeData
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

    private var firestore : FirebaseFirestore? = null
    private var uid : String? = null


    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    var onclickIdx = MutableLiveData<Int>()
    var categoryIdx = MutableLiveData<Int>()
    var allData = MutableLiveData<ArrayList<ProductData>>()
    var riceData = MutableLiveData<ArrayList<ProductData>>()
    var cookieData = MutableLiveData<ArrayList<ProductData>>()
    var breadData = MutableLiveData<ArrayList<ProductData>>()
    var drinkData = MutableLiveData<ArrayList<ProductData>>()

    var getEvent = MutableLiveData<Int>()

    init {
        firestore = FirebaseFirestore.getInstance()
        categoryIdx.value = 0 // 0 : 전체, 1 : 신선제품, 2 : 과자, 3 : 빵, 4 : 음료
        onclickIdx.value = 0 // 0 : 전체, 1 : 신선제품, 2 : 과자, 3 : 빵, 4 : 음료
        allData.value = ArrayList<ProductData>()
        riceData.value = ArrayList<ProductData>()
        cookieData.value = ArrayList<ProductData>()
        breadData.value = ArrayList<ProductData>()
        drinkData.value = ArrayList<ProductData>()

        getEvent.value = 0 // 0 : 기본, 1 : CU, 2: GS, 3: 7eleven
    }

    companion object {
        var listAll = arrayListOf<ProductData>()
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


    fun makeData() {
        Log.d(title, "makeData")
        firestore?.collection("Review")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                listAll.clear()
                if (querySnapshot == null) return@addSnapshotListener

                getEvent.value = 1

                // 데이터 받아오기
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(ProductData::class.java)
                    listAll.add(item!!)
                    var i = 0
                    Log.d(title, "데이터 받아오는중 " + i)
                    i++
                }
            }

    }

    fun initData() = viewModelScope.launch {
        Log.d(title, "initData")
        firestore?.collection("Review")
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우
                Log.d(title, "성공")
                listAll.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    listAll.add(ProductData(Integer.parseInt(document["review_brand_idx"] as String),
                        Integer.parseInt(document["review_category_idx"] as String),
                        Integer.parseInt(document["review_imoji_idx"] as String),
                        document["review_title"] as String,
                        document["review_price"] as String,
                        document["review_cnt"] as String,
                        document["review_cm_id"] as String,
                        document["review_cm_comment"] as String,
                        document["review_img"] as String))
                    Log.d(title, "로그확인 : " + document["review_brand_idx"] as String)
                }
                getEvent.value = 1
            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(title, "Error getting documents: $exception")
            }
    }

    fun initDetailData() = viewModelScope.launch {
        Log.d(title, "initData")
        firestore?.collection("Review_Detail")
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우
                Log.d(title, "성공")
                listAll.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    listAll.add(ProductData(Integer.parseInt(document["review_brand_idx"] as String),
                        Integer.parseInt(document["review_category_idx"] as String),
                        Integer.parseInt(document["review_imoji_idx"] as String),
                        document["review_title"] as String,
                        document["review_price"] as String,
                        document["review_cnt"] as String,
                        document["review_cm_id"] as String,
                        document["review_cm_comment"] as String,
                        document["review_img"] as String))
                    Log.d(title, "로그확인 : " + document["review_brand_idx"] as String)
                }
                getEvent.value = 1
            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(title, "Error getting documents: $exception")
            }
    }



    fun getList(): ArrayList<ProductData> {
        return listAll
    }


}