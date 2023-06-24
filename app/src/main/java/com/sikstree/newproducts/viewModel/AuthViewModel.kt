package com.sikstree.newproducts.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sikstree.newproducts.Data.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(application: Application) : AndroidViewModel(application){


    private val title = "AuthViewModel"

    private val _uiState = MutableStateFlow<UiState>(UiState.LoadingShow)
    val uiState = _uiState.asStateFlow()






}