package com.sikstree.newproduct.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sikstree.newproduct.Data.ReviewData
import com.sikstree.newproduct.Data.UserUtil
import kotlinx.coroutines.launch

class AddReviewViewModel() : ViewModel() {
    private val title = "AddReviewViewModel"

    private var firestore : FirebaseFirestore? = null

    lateinit var storage : FirebaseStorage

    var onclickIdx = MutableLiveData<Int>()
    var btnEnable = MutableLiveData<Boolean>()
    var imgAddEvent = MutableLiveData<Boolean>()
    var finishEvent = MutableLiveData<Boolean>()

    companion object {
        var imgArr = arrayListOf<String>()
        var observeCount = 0
    }

    init {
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        onclickIdx.value = 0 // 0 : 전체, 1 : 신선제품, 2 : 과자, 3 : 빵, 4 : 음료
        btnEnable.value = false
        imgAddEvent.value = false
        finishEvent.value = false
    }


    fun addReview(reviewData : ReviewData) { // 리뷰 데이터 등록
        firestore?.collection("Review_Detail")?.document("[" + reviewData.review_id + "]" + reviewData.review_title)
            ?.set(reviewData)
            ?.addOnSuccessListener { Log.d(title, "성공")
                finishEvent.value = true
            }
            ?.addOnFailureListener { e -> Log.w(title, "Error writing document", e)
            }
    }


    fun uploadImg(imgUri: Uri, imgUri2: Uri, imgUri3: Uri, img_count: Int) = viewModelScope.launch {
        launch {
            if (imgUri != null) uploadPhoto(imgUri!!, img_count)
            if (imgUri2 != null) uploadPhoto(imgUri2!!, img_count)
            if (imgUri3 != null) uploadPhoto(imgUri3!!, img_count)
        }.join()
    }


    private fun uploadPhoto( // 파이어스토어에 사진 업로드 후 성공 시 다시 받아와서 로드
        imageURI: Uri,
        img_count: Int
    ) = viewModelScope.launch {
        val fileName = UserUtil.USER_NAME + "${System.currentTimeMillis()}.png"
        storage.reference.child("photo/" + UserUtil.USER_NAME).child(fileName)
            .putFile(imageURI)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // 파일 업로드에 성공했기 때문에 파일을 다시 받아 오도록 해야함
                    storage.reference.child("photo/" + UserUtil.USER_NAME).child(fileName).downloadUrl
                        .addOnSuccessListener { uri ->
                            Log.d(title, "성공 URI" + uri.toString())
                            imgArr.add(uri.toString())
                            if (imgArr.size == img_count) {
                                imgAddEvent.value = true
                            }
                        }.addOnFailureListener {
                            Log.d(title, "실패 URI")
                        }
                } else {
                    Log.d(title, "실패 1 URI")
                }
            }
    }

    fun getImgArr(): ArrayList<String> { // 이미지 Arr 호출 시 데이터 반환, 이미지 개수에 따라서 3개 미만일 시 나머지 부분은 공백으로 리턴
        if (imgArr.size == 0) {
            imgArr.add("")
            imgArr.add("")
            imgArr.add("")
        } else if (imgArr.size == 1) {
            imgArr.add("")
            imgArr.add("")
        } else if (imgArr.size == 2) {
            imgArr.add("")
        }

        return imgArr
    }

}