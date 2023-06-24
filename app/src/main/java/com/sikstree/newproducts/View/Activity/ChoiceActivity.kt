package com.sikstree.newproducts.View.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.sikstree.newproducts.Adapter.ReviewAdapter
import com.sikstree.newproducts.Data.ReviewData
import com.sikstree.newproducts.R
import com.sikstree.newproducts.databinding.ActivityChoiceBinding
import com.sikstree.newproducts.viewModel.ChoiceViewModel

class ChoiceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChoiceBinding
    private val viewModel : ChoiceViewModel by viewModels()

    lateinit var reviewAdapter: ReviewAdapter
    val datas = mutableListOf<ReviewData>()

    var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choice)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initView()
        observeIcon()
        onClick()

    }

    private fun initView() = with(binding) {
        productName.text = intent.getStringExtra("title")

    }

    companion object {
        var iconIdx = 0
    }

    private fun onClick() = with(binding){
        btnNext.setOnClickListener() {
            if (viewModel?.btnEnable?.value == true) {
                val intentAddReview = Intent(this@ChoiceActivity,AddReviewActivity::class.java)
                intentAddReview.putExtra("iconIdx", iconIdx)
                intentAddReview.putExtra("title", intent.getStringExtra("title"))
                startActivity(intentAddReview)
                finish()
            } else {
                Toast.makeText(this@ChoiceActivity, "아이콘을 클릭해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeIcon() = with(viewModel) {
        onclickIdx.observe(this@ChoiceActivity) {
            when (it) {
                1 -> {
                    with(binding) {
                        iconC1.isSelected = true
                        iconC2.isSelected = false
                        iconC3.isSelected = false
                        iconC4.isSelected = false
                        iconC5.isSelected = false
                        choiceIcon()
                        choiceText.setText("별로에요..")
                        Glide.with(this@ChoiceActivity).load(R.drawable.icon_1).into(choiceImg)
                        iconIdx = 1
                    }
                }
                2 -> {
                    with(binding) {
                        iconC1.isSelected = false
                        iconC2.isSelected = true
                        iconC3.isSelected = false
                        iconC4.isSelected = false
                        iconC5.isSelected = false
                        choiceIcon()
                        choiceText.setText("애매해요..")
                        Glide.with(this@ChoiceActivity).load(R.drawable.icon_2).into(choiceImg)
                        iconIdx = 2
                    }
                }
                3 -> {
                    with(binding) {
                        iconC1.isSelected = false
                        iconC2.isSelected = false
                        iconC3.isSelected = true
                        iconC4.isSelected = false
                        iconC5.isSelected = false
                        choiceIcon()
                        choiceText.setText("보통이에요!")
                        Glide.with(this@ChoiceActivity).load(R.drawable.icon_3).into(choiceImg)
                        iconIdx = 3
                    }
                }
                4 -> {
                    with(binding) {
                        iconC1.isSelected = false
                        iconC2.isSelected = false
                        iconC3.isSelected = false
                        iconC4.isSelected = true
                        iconC5.isSelected = false
                        choiceIcon()
                        choiceText.setText("좋아요!")
                        Glide.with(this@ChoiceActivity).load(R.drawable.icon_4).into(choiceImg)
                        iconIdx = 4
                    }
                }
                5 -> {
                    with(binding) {
                        iconC1.isSelected = false
                        iconC2.isSelected = false
                        iconC3.isSelected = false
                        iconC4.isSelected = false
                        iconC5.isSelected = true
                        choiceIcon()
                        choiceText.setText("최고에요!")
                        Glide.with(this@ChoiceActivity).load(R.drawable.icon_5).into(choiceImg)
                        iconIdx = 5
                    }
                }
            }


        }
    }

    private fun choiceIcon() = with(binding) {
        viewModel?.btnEnable?.value = true
        Glide.with(this@ChoiceActivity).load(R.drawable.btn_next_active).into(btnNext)
        choiceText.visibility = View.VISIBLE
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