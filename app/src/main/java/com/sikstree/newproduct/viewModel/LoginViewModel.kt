package com.sikstree.newproduct.viewModel

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {
    private val title = "LoginViewModel"

    var serverStatus = MutableLiveData<String>()
    var serverHostTxt = MutableLiveData<String>()

    private val TAG = this.javaClass.simpleName

    private lateinit var firebaseAuth: FirebaseAuth
    private var email: String = ""
    private var tokenId: String? = null

    init {
        serverStatus.value = ""
        serverHostTxt.value = ""

    }


    fun initFirebase(result: ActivityResult) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            task.getResult(ApiException::class.java)?.let { account ->
                tokenId = account.idToken
                if (tokenId != null && tokenId != "") {
                    val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                    firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (firebaseAuth.currentUser != null) {
                                val user: FirebaseUser = firebaseAuth.currentUser!!
                                email = user.email.toString()
                                Log.e(TAG, "email : $email")
                                val googleSignInToken = account.idToken ?: ""
                                if (googleSignInToken != "") {
                                    Log.e(TAG, "googleSignInToken : $googleSignInToken")
                                } else {
                                    Log.e(TAG, "googleSignInToken이 null")
                                }
                            }
                        }
                }
            } ?: throw Exception()
        }   catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun googleSignIn(activity: Activity, idToken: String, launcher: ActivityResultLauncher<Intent>) {
        CoroutineScope(Dispatchers.IO).launch {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(idToken)
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(activity, gso)
            val signInIntent: Intent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }




}