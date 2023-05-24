package com.sikstree.newproduct.View.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.sikstree.newproduct.R
import com.sikstree.newproduct.databinding.ActivityLoginBinding
import com.sikstree.newproduct.databinding.ActivityStartBinding
import com.sikstree.newproduct.viewModel.StartViewModel

class StartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStartBinding
    private val viewModel : StartViewModel by viewModels()
    private val fragmentManager : FragmentManager = supportFragmentManager

    var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

//        viewModel.setFragment(TAG_HOME, HomeFragment(), fragmentManager)

        initView()
    }

    private fun initView() {
        val name : String = "불타는삼각김밥"
        val builder_main = SpannableStringBuilder(name + "님\n프로필을 선택해 주세요!")
        val builder_welcome = SpannableStringBuilder("어서오세요!\n" + name + "님")

        val colorBlueSpan = ForegroundColorSpan(Color.parseColor("#0CFBB2"))
        builder_main.setSpan(colorBlueSpan, 0, name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder_welcome.setSpan(colorBlueSpan, 7, name.length + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.loginText.setText(builder_main)
        binding.loginTextWelcome.setText(builder_welcome)

        var listManager = GridLayoutManager(this, 4)
        var listAdapter = viewModel.ListAdapterGrid()


        var recyclerList = binding.recyclerGridView.apply {
            setHasFixedSize(true)
            layoutManager = listManager
            adapter = listAdapter
        }


        binding.btnStartActive.setOnClickListener() {
//            binding.loginMainLayout.visibility = View.GONE
            binding.loginWelcomeLayout.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)

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