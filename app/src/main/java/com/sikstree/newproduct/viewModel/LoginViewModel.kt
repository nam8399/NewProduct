package com.sikstree.newproduct.viewModel

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.sikstree.newproduct.Data.LoginState
import kotlinx.coroutines.*

class LoginViewModel() : ViewModel() {
    private val title = "LoginViewModel"
    var uidList: ArrayList<String> = arrayListOf()

    private var _loginStateLiveData = MutableLiveData<LoginState>(LoginState.UnInitialized)
    val loginStateLiveData: LiveData<LoginState> = _loginStateLiveData
    var login_check = MutableLiveData<Boolean>()

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null
    private var uid : String? = null

    init {
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid
        login_check.value = false
    }

    fun fetchData(tokenId: String?): Job = viewModelScope.launch {
        setState(LoginState.Loading)
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

    /* 로그인 성공 후 정보 설정 */
    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let { user ->
            setState(
                LoginState.Success.Registered(
                    user.displayName ?: "익명",
                    user.photoUrl!!,
                )
            )
        } ?: kotlin.run {
            setState(LoginState.Success.NotRegistered)
        }
    }

    fun getLoginState() = viewModelScope.launch {
        firestore?.collection("UserID")
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    if (uid?.equals(document["uid"] as String)!!) {
                        Log.d(title, "Login check - " + document["uid"] as String)
                        login_check.value = true
                        break
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



//    fun initFirebase(result: ActivityResult) {
//        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//        try {
//            task.getResult(ApiException::class.java)?.let { account ->
//                tokenId = account.idToken
//                if (tokenId != null && tokenId != "") {
//                    val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
//                    firebaseAuth.signInWithCredential(credential)
//                        .addOnCompleteListener {
//                            if (firebaseAuth.currentUser != null) {
//                                val user: FirebaseUser = firebaseAuth.currentUser!!
//                                email = user.email.toString()
//                                Log.e(TAG, "email : $email")
//                                val googleSignInToken = account.idToken ?: ""
//                                if (googleSignInToken != "") {
//                                    Log.e(TAG, "googleSignInToken : $googleSignInToken")
//                                } else {
//                                    Log.e(TAG, "googleSignInToken이 null")
//                                }
//                            }
//                        }
//                }
//            } ?: throw Exception()
//        }   catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun googleSignIn(activity: Activity, idToken: String, launcher: ActivityResultLauncher<Intent>) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(idToken)
//                .requestEmail()
//                .build()
//            val googleSignInClient = GoogleSignIn.getClient(activity, gso)
//            val signInIntent: Intent = googleSignInClient.signInIntent
//            launcher.launch(signInIntent)
//        }
//    }




}