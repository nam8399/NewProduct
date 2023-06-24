package com.sikstree.newproducts.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.sikstree.newproducts.Data.*
import kotlinx.coroutines.launch
import kotlin.math.round

class ProductViewModel(application: Application) : AndroidViewModel(application){


    private val title = "ProductViewModel"

    private var firestore : FirebaseFirestore? = null
    private var uid : String? = null


    private val _uiState = MutableLiveData<UiState>(UiState.LoadingShow)
    val uiState: LiveData<UiState> = _uiState

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
        var listReviewAll = arrayListOf<ReviewData>()
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
                viewModelScope.launch {
                    for (document in result) {  // 가져온 문서들은 result에 들어감
                        val brandIdx = Integer.parseInt(document["review_brand_idx"] as String)
                        val categoryIdx = Integer.parseInt(document["review_category_idx"] as String)
                        val title = document["review_title"] as String
                        val price = document["review_price"] as String
                        val img = document["review_img"] as String

                        initDetailData(brandIdx, title, categoryIdx, price, img).join()

                        Log.d(title, "로그확인 : " + document["review_brand_idx"] as String)
                    }
                    getEvent.value = 1
                }
            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(title, "Error getting documents: $exception")
            }
    }

    fun initDetailData(
        brandIdx : Int,
        name: String,
        categoryIdx: Int,
        price: String,
        img: String
    ) = viewModelScope.launch {
            Log.d(this@ProductViewModel.title, "initDetailData")
            firestore?.collection("Review_Detail")?.whereEqualTo("review_title", name)
                ?.get()      // 문서 가져오기
                ?.addOnSuccessListener { result ->
                    // 성공할 경우
                    Log.d(this@ProductViewModel.title, "성공")
                    listReviewAll.clear()
                    var imojiAvgSum = 0
                    var imojiAvg = 0
                    var reviewId = ""
                    var reviewComment = ""
                    for (document in result) {  // 가져온 문서들은 result에 들어감)
                        listReviewAll.add(ReviewData((document["review_profile"] as Long).toInt(),
                            document["review_id"] as String,
                            (document["review_imoji"] as Long).toInt(),
                            document["review_title"] as String,
                            document["review_comment"] as String,
                            document["review_date"] as String,
                            document["review_img"] as String,
                            document["review_img2"] as String,
                            document["review_img3"] as String))
                        imojiAvgSum += (document["review_imoji"] as Long).toInt()
                        Log.d(this@ProductViewModel.title, "로그확인 : " + document["review_title"] as String)
                    }
                    if (listReviewAll.size != 0) {
                        imojiAvg = round((imojiAvgSum/ listReviewAll.size).toDouble()).toInt()
                        reviewId = listReviewAll[0].review_id
                        reviewComment = listReviewAll[0].review_comment
                    }

                    listAll.add(ProductData(brandIdx,
                        categoryIdx,
                        imojiAvg,
                        name,
                        price,
                        listReviewAll.size.toString(),
                        reviewId,
                        reviewComment,
                        img
                    ))
                    getEvent.value = 1
                }
                ?.addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.w(this@ProductViewModel.title, "Error getting documents: $exception")
                }
        }




    fun getList(): ArrayList<ProductData> {
        return listAll
    }

    private fun setState(state: UiState) {
        _uiState.postValue(state)
    }


}