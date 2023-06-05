package com.sikstree.newproduct.View.Activity

import android.app.Activity
import android.content.Intent
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
import com.sikstree.newproduct.Data.ReviewData
import com.sikstree.newproduct.Data.UserUtil
import com.sikstree.newproduct.R
import com.sikstree.newproduct.databinding.ActivityAddreviewBinding
import com.sikstree.newproduct.viewModel.AddReviewViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddReviewActivity : AppCompatActivity() {
    private val title = "AddReviewActivity"
    private lateinit var binding : ActivityAddreviewBinding
    private val mViewModel : AddReviewViewModel by viewModels()


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
    }


    private fun onClick() = with(binding){
        btnSkip.setOnClickListener() {
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
            mViewModel?.uploadImg(imgUri.toUri(), imgUri2.toUri(), imgUri3.toUri(), img_count)
        }

        mViewModel?.imgAddEvent?.observe(this@AddReviewActivity) {
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

        mViewModel.finishEvent.observe(this@AddReviewActivity) {
            if (it) {
                finish()
                UserUtil.PRODUCT_VIEW_RESET = true
            }
        }

        btnImgadd.setOnClickListener {
            navigatePhotos()
        }


    }


    fun getTime() : String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val formatted = current.format(formatter)
        return formatted
    }


    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
                        binding.btnImg1.setImageBitmap(bitmap)
                        binding.btnImg1.visibility = View.VISIBLE
                        imgUri = currentImgUrl.toString()!!
                        img_count++
                    }
                    1 -> {
                        binding.btnImg2.setImageBitmap(bitmap)
                        binding.btnImg2.visibility = View.VISIBLE
                        img_count++
                        imgUri2 = currentImgUrl.toString()!!
                    }
                    2 -> {
                        binding.btnImg3.setImageBitmap(bitmap)
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
    }


}