package com.sikstree.newproduct.View.Activity

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.appsearch.SetSchemaRequest.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.graphics.Color
import android.graphics.Insets.add
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.sikstree.newproduct.Adapter.ReviewAdapter
import com.sikstree.newproduct.Data.ProductData
import com.sikstree.newproduct.Data.ReviewData
import com.sikstree.newproduct.R
import com.sikstree.newproduct.databinding.ActivityAddreviewBinding
import com.sikstree.newproduct.databinding.ActivityChoiceBinding
import com.sikstree.newproduct.databinding.ActivityReviewBinding
import com.sikstree.newproduct.viewModel.AddReviewViewModel
import com.sikstree.newproduct.viewModel.ChoiceViewModel
import com.sikstree.newproduct.viewModel.ReviewViewModel

class AddReviewActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddreviewBinding
    private val viewModel : AddReviewViewModel by viewModels()
    private var img_count = 0


    private val permissionList = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        result.forEach {
            if(!it.value) {
                Toast.makeText(applicationContext, "권한 동의 필요!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addreview)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        onClick()

        checkPermission.launch(permissionList)
    }


    private fun onClick() = with(binding){
        btnSkip.setOnClickListener() {
            val intent = Intent(this@AddReviewActivity, MainActivity::class.java)
            startActivity(intent)
        }

        btnImgadd.setOnClickListener {
            if (img_count==0) { readImage(btnImg1).launch("image/*") }
            else if (img_count==1) { readImage(btnImg2).launch("image/*") }
            else if (img_count==2) { readImage(btnImg3).launch("image/*") }
        }


    }


    private fun readImage(img : ImageView) = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        Glide.with(this@AddReviewActivity).load(uri).into(img)
        img_count++
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