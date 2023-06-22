package com.sikstree.newproduct.viewModel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.sikstree.newproduct.Data.UiState
import com.sikstree.newproduct.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val title = "MainViewModel"

    private val _uiState = MutableStateFlow<UiState>(UiState.LoadingShow)
    val uiState = _uiState.asStateFlow()



    companion object {
        const val TAG_HOME = "home_fragment"
        const val TAG_PRODUCT = "product_fragment"
        const val TAG_AUTH = "auth_fragment"
    }



    fun setFragment(tag: String, fragment: Fragment, fragmentManager: FragmentManager) {
        val manager: FragmentManager = fragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val home = manager.findFragmentByTag(Companion.TAG_HOME)
        val product = manager.findFragmentByTag(Companion.TAG_PRODUCT)
        val about = manager.findFragmentByTag(Companion.TAG_AUTH)

        if (home != null){
            fragTransaction.hide(home)
        }

        if (product != null){
            fragTransaction.hide(product)
        }

        if (about != null) {
            fragTransaction.hide(about)
        }

        if (tag == Companion.TAG_HOME) {
            if (home != null) {
                fragTransaction.show(home)
            }
        }

        else if (tag == Companion.TAG_PRODUCT) {
            if (product != null) {
                fragTransaction.show(product)
            }
        }

        else if (tag == Companion.TAG_AUTH){
            if (about != null){
                fragTransaction.show(about)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }




}