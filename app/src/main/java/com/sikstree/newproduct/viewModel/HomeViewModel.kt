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
import com.sikstree.newproduct.Data.MyApplication
import com.sikstree.newproduct.Data.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class HomeViewModel( application: Application) : AndroidViewModel(application){


    private val title = "HomeViewModel"

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null
    private var uid : String? = null

    var name = MutableLiveData<String>()



    init {
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid
        name.value = ""
    }


    fun getName() = viewModelScope.launch {
        firestore?.collection("UserID")
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    if (uid?.equals(document["uid"] as String)!!) {
                        Log.d(title, "name - " + document["name"] as String)
                        name.value = document["name"] as String
                        break
                    }
                }
            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(title, "Error getting documents: $exception")
            }
    }





}