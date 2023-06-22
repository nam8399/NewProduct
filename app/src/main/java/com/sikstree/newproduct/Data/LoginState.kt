package com.sikstree.newproduct.Data

import android.net.Uri

sealed class LoginState {

    object UnInitialized : LoginState()

    object LoadingShow : LoginState()
    object LoadingDismiss : LoginState()

    data class Login(
        val idToken: String
    ) : LoginState()

    sealed class Success : LoginState() {
        data class Registered(  //Google Auth 등록된 상태
            val userName: String,
            val profileImgeUri: Uri,
        ) : Success()
        object NotRegistered : Success()  //Google Auth 미등록된 상태
    }

    object Error : LoginState()

}