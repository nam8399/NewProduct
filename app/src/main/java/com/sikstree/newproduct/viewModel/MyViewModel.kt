package com.sikstree.newproduct.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.sikstree.minecraftstatus.Model.Event
import com.sikstree.newproduct.Data.LoginData
import com.sikstree.newproduct.Data.MyApplication
import com.sikstree.newproduct.Data.UiState
import com.sikstree.newproduct.Data.UserUtil
import com.sikstree.newproduct.View.Activity.MainActivity
import com.sikstree.newproduct.View.Dialog.CustomDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MyViewModel(application: Application) : AndroidViewModel(application){


    private val title = "MyViewModel"

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null

    var isSecession = MutableLiveData<Boolean>() // 회원탈퇴 성공


    init {
        firestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        isSecession.value = false
    }

    fun logoutFireStore() = viewModelScope.launch {
        var loginData = LoginData()
        loginData.uid = UserUtil.USER_ID
        loginData.name = UserUtil.USER_NAME
        loginData.imoji = UserUtil.USER_PROFILE_IDX
        loginData.autoLogin = "0"

        firestore?.collection("UserID")?.document(auth!!.currentUser!!.uid)?.set(loginData)
//        Toast.makeText(this,"저장완료",Toast.LENGTH_SHORT).show()

        Log.d(title, "로그아웃 완료 - " + auth!!.uid.toString())

        auth!!.signOut()
    }


    fun secessionFireStore() = viewModelScope.launch {
//        var loginData = LoginData()
//        loginData.uid = UserUtil.USER_ID
//        loginData.name = UserUtil.USER_NAME
//        loginData.imoji = UserUtil.USER_PROFILE_IDX
//        loginData.autoLogin = "0"
//
//        firestore?.collection("UserID")?.document(auth!!.currentUser!!.uid)?.set(loginData)
//        Toast.makeText(this,"저장완료",Toast.LENGTH_SHORT).show()

        val user = auth!!.currentUser!!
        user.delete().addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                Log.d(title, "회원탈퇴 완료 - " + auth!!.uid.toString())
                isSecession.value = true
            } else {
                Log.d(title, "회원탈퇴 실패 - " + auth!!.uid.toString())
            }
        }
    }


}