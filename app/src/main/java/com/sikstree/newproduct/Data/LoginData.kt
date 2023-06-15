package com.sikstree.newproduct.Data

data class LoginData (
    var uid :String? = null,
    var name : String? = null,
    var imoji : Int? = null,
    var autoLogin : String? = null // 자동로그인 0이면 로그아웃 상태, 1이면 자동로그인 상태
)
