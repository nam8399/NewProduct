package com.sikstree.newproduct.View.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.sikstree.newproduct.R
import com.sikstree.newproduct.databinding.ActivityLoginBinding
import com.sikstree.newproduct.databinding.ActivityStartBinding
import com.sikstree.newproduct.viewModel.StartViewModel

class StartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStartBinding
    private val viewModel : StartViewModel by viewModels()
    private val title = "StartActivity"
    private val fragmentManager : FragmentManager = supportFragmentManager

    var backPressedTime : Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

//        viewModel.setFragment(TAG_HOME, HomeFragment(), fragmentManager)

        initName()
    }




    private fun initName() {
        var nameFisrtList = arrayListOf("주문하신","놓고가신","1+1","2+1","오픈런","마지막","존맛탱","할인된","정가로산","카카오페이로산","더맛있는","맴버십할인된","계산한","봉지없는",
                                        "새로산","남다른","행사상품","방금다먹은","신선한","기대되는","큰","미지근한","빨대빠진","새로운","네모난","쫀득한","귀여운","멋진","부드러운","기름진")
        var nameSecondList = arrayListOf("삼각김밥","도시락","케이크","주스","비스켓","도넛","젤리","사탕","껌","컵라면","누들면","우유","초콜렛","즉석커피","샌드위치","꼬치","교통카드","스트링치즈",
                                        "컵밥","우산","햄버거","커피","냉동식품","즉석식품","통조림","피로회복제","아이스크림","견과류","일회용품","세면도구")

        var name = nameFisrtList.get(randomInt()) + nameSecondList.get(randomInt())
        Log.d(title, "생성 닉네임 : " + name)
        viewModel.getNameCheck(name)

        viewModel.name_check.observe(this@StartActivity) {
            if(it == 1) initView(name) else if(it == 2) initName()
        }
    }

    fun randomInt(): Int {
        val range = (0..29)  // 1 <= n <= 15
        println(range.random())
        return range.random()
    }

    private fun initView(name : String) {
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


        viewModel.iconClickPosition.observe(this) {
            if (it > -1) {
                binding.btnStartActive.isSelected = true
                binding.btnStartActive.isEnabled = true
            }
        }


        binding.btnStartActive.setOnClickListener() {
//            binding.loginMainLayout.visibility = View.GONE
            binding.loginWelcomeLayout.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                viewModel.uploadFirebase(name)
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