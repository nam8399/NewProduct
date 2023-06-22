package com.sikstree.newproduct.Data

sealed class UiState {
    object LoadingShow : UiState()
    object LoadingDismiss : UiState()
    object Empty : UiState()
//    data class Success(val list: List<FavoriteItem>) : UiState()
    data class Success(val message: String?) : UiState()
    data class Error(val message: String?) : UiState()
}