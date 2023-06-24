package com.sikstree.newproducts.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.sikstree.newproducts.Data.HomeData
import com.sikstree.newproducts.Data.UiState
import com.sikstree.newproducts.Data.UserUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel( application: Application) : AndroidViewModel(application) {


    private val title = "HomeViewModel"

    private val _uiState = MutableStateFlow<UiState>(UiState.LoadingShow)
    val uiState = _uiState.asStateFlow()
    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null
    private var uid: String? = null
    var login_check = MutableLiveData<Boolean>()

    var name = MutableLiveData<String>()
    var getEvent = MutableLiveData<Int>()


    init {
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid
        name.value = ""
        getEvent.value = 0 // 0 : 기본, 1 : CU 이미지, 2: GS 이미지, 3: 7eleven 이미지
    }

    companion object {
        var listCU = ArrayList<HomeData>()
        var listGS = ArrayList<HomeData>()
        var list7 = ArrayList<HomeData>()
    }


    fun getName() = viewModelScope.launch {
        firestore?.collection("UserID")
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    if (uid?.equals(document["uid"] as String)!!) {
                        Log.d(title, "name - " + document["name"] as String)
                        name.value = document["name"] as String
                        UserUtil.USER_NAME = document["name"] as String
                        UserUtil.USER_PROFILE_IDX = (document["imoji"] as Long).toInt()
                        UserUtil.USER_ID = document["uid"] as String
                        break
                    }
                }
            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(title, "Error getting documents: $exception")
            }
    }

    fun getCU() = viewModelScope.launch {
        firestore?.collection("HomeCU")
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우
                listCU.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    listCU.add(HomeData(document["img"] as String, document["name"] as String))
                    Log.d(title, "로그확인 : " + document["img"] as String)
                }
                getEvent.value = 1
            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(title, "Error getting documents: $exception")
            }
    }


    fun getGS() = viewModelScope.launch {
        firestore?.collection("HomeGS")
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우
                listGS.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    listGS.add(HomeData(document["img"] as String, document["name"] as String))
                }


                getEvent.value = 2

            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(title, "Error getting documents: $exception")
            }
    }


    fun get7() = viewModelScope.launch {
        firestore?.collection("Home7")
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우
                list7.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    list7.add(HomeData(document["img"] as String, document["name"] as String))
                }

                getEvent.value = 3

            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(title, "Error getting documents: $exception")
            }
    }


    fun getListCU(): ArrayList<HomeData> {
        return listCU
    }


    fun getListGS(): ArrayList<HomeData> {
        return listGS
    }


    fun getList7(): ArrayList<HomeData> {
        return list7
    }
}