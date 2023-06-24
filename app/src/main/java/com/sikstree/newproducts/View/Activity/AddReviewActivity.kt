package com.sikstree.newproducts.View.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.sikstree.newproducts.Data.ReviewData
import com.sikstree.newproducts.Data.UserUtil
import com.sikstree.newproducts.R
import com.sikstree.newproducts.View.Dialog.CustomLoadingDialog
import com.sikstree.newproducts.databinding.ActivityAddreviewBinding
import com.sikstree.newproducts.viewModel.AddReviewViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddReviewActivity : AppCompatActivity() {
    private val title = "AddReviewActivity"
    private lateinit var binding : ActivityAddreviewBinding
    private val mViewModel : AddReviewViewModel by viewModels()
    private lateinit var loadingAnimDialog : CustomLoadingDialog


    var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addreview)
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this

        initView()
        onClick()
    }

    companion object {
        var imgUri = ""
        var imgUri2 = ""
        var imgUri3 = ""
        private var img_count = 0
    }


    private fun initView() = with(binding) {
        choiceProductName.text = intent.getStringExtra("title")

        loadingAnimDialog = CustomLoadingDialog(this@AddReviewActivity)
        loadingAnimDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    private fun onClick() = with(binding){
        btnSkip.setOnClickListener() {
            loadingAnimDialog.show()
            mViewModel?.addReview(ReviewData(UserUtil.USER_PROFILE_IDX,
                UserUtil.USER_NAME,
                intent.getIntExtra("iconIdx", 0),
                intent.getStringExtra("title").toString(),
                "",
                getTime(),
                "",
                "",
                ""
            ))
            UserUtil.PRODUCT_VIEW_RESET = true
        }

        btnAdd.setOnClickListener {
            loadingAnimDialog.show()
            mViewModel?.uploadImg(imgUri.toUri(), imgUri2.toUri(), imgUri3.toUri(), img_count) // 이미지 등록 버튼 클릭 시 갤러리에서 가져온 이미지 서버에 등록
        }

        mViewModel?.imgAddEvent?.observe(this@AddReviewActivity) { // 이미지 저장 이벤트 받으면 데이터 가져온 뒤 셋팅
            if (it) {
                mViewModel?.addReview(ReviewData(UserUtil.USER_PROFILE_IDX,
                    UserUtil.USER_NAME,
                    intent.getIntExtra("iconIdx", 0),
                    intent.getStringExtra("title").toString(),
                    reviewEditText.text.toString(),
                    getTime(),
                    mViewModel.getImgArr().get(0),
                    mViewModel.getImgArr().get(1),
                    mViewModel.getImgArr().get(2)
                ))
            }

        }

        mViewModel.finishEvent.observe(this@AddReviewActivity) { // 액티비티 종료 이벤트 받으면 액티비티 종료
            if (it) {
                loadingAnimDialog.dismiss()
                finish()
                UserUtil.PRODUCT_VIEW_RESET = true
                img_count = 0
            }
        }

        btnImgadd.setOnClickListener {
            navigatePhotos()
        }


    }


    fun getTime() : String { // 파이어스토어 이미지 저장 시 이름에 날짜 들어가게 하기 위해 현재 시간 계산
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val formatted = current.format(formatter)
        return formatted
    }


    private fun navigatePhotos() { // 갤러리 이미지 접근
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // 갤러리 이미지 등록 처리
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) {
            Toast.makeText(this,"잘못된 접근입니다",Toast.LENGTH_SHORT).show()
            return
        }

        if (requestCode == 2000) {
            var currentImgUrl : Uri? = data?.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,currentImgUrl)

                when(img_count) {
                    0 -> {
//                        binding.btnImg1.setImageBitmap(bitmap)
                        Glide.with(this).load(currentImgUrl).into(binding.btnImg1) // Glide를 사용하지 않으면 갤러리 이미지 돌아감
                        binding.btnImg1.visibility = View.VISIBLE
                        imgUri = currentImgUrl.toString()!!
                        img_count++
                    }
                    1 -> {
//                        binding.btnImg2.setImageBitmap(bitmap)
                        Glide.with(this).load(currentImgUrl).into(binding.btnImg2)
                        binding.btnImg2.visibility = View.VISIBLE
                        img_count++
                        imgUri2 = currentImgUrl.toString()!!
                    }
                    2 -> {
//                        binding.btnImg3.setImageBitmap(bitmap)
                        Glide.with(this).load(currentImgUrl).into(binding.btnImg3)
                        binding.btnImg3.visibility = View.VISIBLE
                        img_count++
                        imgUri3 = currentImgUrl.toString()!!
                    }
                    3 -> {
                        Toast.makeText(this@AddReviewActivity, "이미지 등록은 3장까지 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Log.d("AddReviewActivity", "Something Wrong")
        }
    }



    override fun onBackPressed() {
        //2.5초이내에 한 번 더 뒤로가기 클릭 시
        if (System.currentTimeMillis() - backPressedTime < 2500) {
            super.onBackPressed()
            return
        }
        Toast.makeText(this, "한번 더 클릭 시 홈으로 이동됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
        img_count = 0
    }


}