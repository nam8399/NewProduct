package com.sikstree.newproduct.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sikstree.minecraftstatus.Model.Event
import com.sikstree.newproduct.Data.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class HomeViewModel( application: Application) : AndroidViewModel(application){

// ViewModel()을 상속받을 경우
// class MainViewModel():ViewModel(){}

    //LiveData
//값이 변경되는 경우 MutableLiveData로 선언한다.

    private val title = "HomeViewModel"

    var testText = MutableLiveData<String>()
    var serverStatus = MutableLiveData<String>()
    var serverHostTxt = MutableLiveData<String>()
    var serverVersion = MutableLiveData<String>()
    var serverPeople = MutableLiveData<String>()
    var serverInputHost = MutableLiveData<String>()
    var isServerAdd = MutableLiveData<Boolean>()
    var serverName = MutableLiveData<String>()
    var serverEditionIndex = MutableLiveData<Int>()
    var showDialog = MutableLiveData<Boolean>() // 다이얼로그를 띄우기 위한 LiveData 변수

    private val _event = MutableLiveData<Event<Boolean>>()


    init {
        testText.value = ""
        serverStatus.value = ""
        serverHostTxt.value = ""
        serverVersion.value = ""
        serverPeople.value = ""
        serverInputHost.value = ""
        serverName.value = ""
        isServerAdd.value = false
        serverEditionIndex.value = 0
        showDialog.value = false
    }

    val event: LiveData<Event<Boolean>>
        get() = _event

    fun onEvent(isAdd : Boolean) {
        _event.value = Event(isAdd)

        if (!isAdd) {
            MyApplication.prefs.setString("serverHost", "")
            MyApplication.prefs.setString("serverName", "")
            MyApplication.prefs.setString("serverEdition", "")

            testText.value = ""
            serverStatus.value = ""
            serverHostTxt.value = ""
            serverVersion.value = ""
            serverPeople.value = ""
            serverInputHost.value = ""
            serverName.value = ""
            serverEditionIndex.value = 0
        }
    }





}