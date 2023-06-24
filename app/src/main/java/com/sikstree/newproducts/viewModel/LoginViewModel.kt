package com.sikstree.newproducts.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.sikstree.newproducts.Data.LoginData
import com.sikstree.newproducts.Data.LoginState
import com.sikstree.newproducts.Data.UserUtil
import kotlinx.coroutines.*

class LoginViewModel() : ViewModel() {
    private val title = "LoginViewModel"
    var uidList: ArrayList<String> = arrayListOf()

    private var _loginStateLiveData = MutableLiveData<LoginState>(LoginState.UnInitialized)
    val loginStateLiveData: LiveData<LoginState> = _loginStateLiveData
    var login_check = MutableLiveData<Int>() // 기본 0, 성공 1, 실패 2

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null
    private var uid : String? = null

    init {
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid
        login_check.value = 0
    }

    fun fetchData(tokenId: String?): Job = viewModelScope.launch {
//        setState(LoginState.Loading)
        tokenId?.let {
            setState(
                LoginState.Login(it)
            )
        } ?: kotlin.run {
            setState(
                LoginState.Success.NotRegistered
            )
        }
    }

    /* 로그인 성공 result 받았을 떄 호출 */
    fun saveToken(idToken: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            fetchData(idToken)
        }
    }

    fun uploadFirebase() {
        var loginData = LoginData()
        loginData.uid = auth?.currentUser?.uid
        loginData.name = UserUtil.USER_NAME
        loginData.imoji = UserUtil.USER_PROFILE_IDX
        loginData.autoLogin = "1"

        firestore?.collection("UserID")?.document(auth!!.currentUser!!.uid)?.set(loginData)
//        Toast.makeText(this,"저장완료",Toast.LENGTH_SHORT).show()
        Log.d(title, "파이어베이스 저장완료 - " + auth!!.uid.toString())
    }


    fun getLogoutState() = viewModelScope.launch {
        firestore?.collection("UserID")
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    if (uid?.equals(document["uid"] as String)!!) {
                        UserUtil.USER_ID = document["uid"] as String
                        UserUtil.USER_NAME = document["name"] as String
                        UserUtil.USER_PROFILE_IDX = (document["imoji"] as Long).toInt()

                        if ("0".equals(document["autoLogin"] as String)) {
                            login_check.value = 2
                            break
                        }

                        Log.d(title, "Login check - " + document["uid"] as String)
                        login_check.value = 1
                        break
                    }
                }
            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(title, "Error getting documents: $exception")
            }
    }

    fun getLoginState() = viewModelScope.launch {
        firestore?.collection("UserID")
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우

                try {
                    for (document in result) {  // 가져온 문서들은 result에 들어감
                        if (uid?.equals(document["uid"] as String)!!) {
                            login_check.value = 1
                            break
                        }
                    }

                    if (login_check.value != 1) {
                        login_check.value = 2
                    }
                } catch (e : Exception) {
                    e.printStackTrace()

                    try {
                        for (document in result) {  // 가져온 문서들은 result에 들어감
                            if (UserUtil.USER_ID.equals(document["uid"] as String)!!) {
                                login_check.value = 1
                                break
                            }
                        }

                        if (login_check.value != 1) {
                            login_check.value = 2
                        }
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                }
            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(title, "Error getting documents: $exception")
            }
    }




    /* 로그아웃 버튼 클릭 시 호출 */
    fun signOut() = viewModelScope.launch {
        fetchData(null)
    }

    private fun setState(state: LoginState) {
        _loginStateLiveData.postValue(state)
    }
}