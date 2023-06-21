package com.sikstree.newproduct.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.sikstree.newproduct.Data.LoginData
import com.sikstree.newproduct.Data.UiState
import com.sikstree.newproduct.Data.UserUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


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

        firestore?.collection("UserID")
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    if (auth!!.uid?.equals(document["uid"] as String)!!) {
                        document.reference.delete()
                        break
                    }
                }

                Log.d(title, "회원 데이터 삭제 완료 - " + auth!!.uid.toString())

                val user = auth!!.currentUser!!
                user.delete().addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        Log.d(title, "회원탈퇴 완료 - " + auth!!.uid.toString())
                        isSecession.value = true
                    } else {
                        Log.d(title, "회원탈퇴 실패 - " + auth!!.uid.toString())
                        isSecession.value = true
                    }
                }
            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(title, "Error getting documents: $exception")
            }
    }


}