package com.sikstree.newproduct.View.Activity

import android.content.Intent
import android.graphics.Insets.add
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sikstree.newproduct.Adapter.ReviewAdapter
import com.sikstree.newproduct.Data.ProductData
import com.sikstree.newproduct.Data.ReviewData
import com.sikstree.newproduct.R
import com.sikstree.newproduct.databinding.ActivityReviewBinding
import com.sikstree.newproduct.viewModel.ReviewViewModel

class ReviewActivity : AppCompatActivity() {
    private lateinit var binding : ActivityReviewBinding
    private val mViewModel : ReviewViewModel by viewModels()

    lateinit var reviewAdapter: ReviewAdapter
    val datas = mutableListOf<ReviewData>()

    var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review)
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this



        initView()
//        initRecycler()
    }

    private fun initView() = with(binding) {
        mViewModel?.initDetailData(intent.getStringExtra("title").toString())
        btnX.setOnClickListener { finish() }
        btnReview.setOnClickListener {
            val intentChoice = Intent(this@ReviewActivity, ChoiceActivity::class.java)
            intentChoice.putExtra("title", intent.getStringExtra("title"))
            startActivity(intentChoice)
            finish()
        }

        mViewModel?.getEvent?.observe(this@ReviewActivity) {
            when(it) {
                1 -> {
                    for (i in 0..mViewModel.getList().size-1) {
                        datas.add(mViewModel.getList().get(i))
                    }
                    initRecycler()
                }
            }
        }

    }

    private fun initRecycler() {
        reviewAdapter = ReviewAdapter(this)
        binding.reviewRecycler.adapter = reviewAdapter

        var imgList = ArrayList<Int>()
        imgList.add(R.drawable.banner_review)
        imgList.add(R.drawable.banner_review)
        imgList.add(R.drawable.banner_review)

        datas.apply {
//            add(ReviewData("", "불타는삼각김밥", 1,"", "2023.05.24", false))
//            add(ReviewData("", "불타는삼각김밥", 2,"지립니다.", "2023.05.23", false))
//            add(ReviewData("", "불타는삼각김밥", 3,"지립니다.", "2023.05.22", true))



            reviewAdapter.datas = datas
            reviewAdapter.notifyDataSetChanged()

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