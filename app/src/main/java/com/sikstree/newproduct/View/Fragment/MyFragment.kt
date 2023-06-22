package com.sikstree.newproduct.View.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.sikstree.newproduct.Data.UiState
import com.sikstree.newproduct.Data.UserUtil
import com.sikstree.newproduct.R
import com.sikstree.newproduct.View.Activity.AuthActivity
import com.sikstree.newproduct.View.Activity.LoginActivity
import com.sikstree.newproduct.View.Activity.MainActivity
import com.sikstree.newproduct.View.Activity.WebviewActivity
import com.sikstree.newproduct.View.Dialog.CustomDialog
import com.sikstree.newproduct.databinding.FragmentMyBinding
import com.sikstree.newproduct.viewModel.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyFragment() : Fragment() {
    lateinit var binding : FragmentMyBinding
    lateinit var viewModel : MyViewModel
    var isSeverAdd : Boolean = false
    private val title = "MyFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    init{
        instance = this
    }

    companion object{
        private var instance: MyFragment? = null
        fun getInstance():MyFragment?{
            return instance
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false)

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel



        return binding.root
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        showSomething()



    }

    private fun initView() = with(binding) {
        myName.text = UserUtil.USER_NAME

        when(UserUtil.USER_PROFILE_IDX){
            0 -> Glide.with(this@MyFragment).load(R.drawable.imoji_1).into(myIcon)
            1 -> Glide.with(this@MyFragment).load(R.drawable.imoji_2).into(myIcon)
            2 -> Glide.with(this@MyFragment).load(R.drawable.imoji_3).into(myIcon)
            3 -> Glide.with(this@MyFragment).load(R.drawable.imoji_4).into(myIcon)
            4 -> Glide.with(this@MyFragment).load(R.drawable.imoji_5).into(myIcon)
            5 -> Glide.with(this@MyFragment).load(R.drawable.imoji_6).into(myIcon)
            6 -> Glide.with(this@MyFragment).load(R.drawable.imoji_7).into(myIcon)
            7 -> Glide.with(this@MyFragment).load(R.drawable.imoji_8).into(myIcon)
            8 -> Glide.with(this@MyFragment).load(R.drawable.imoji_9).into(myIcon)
            9 -> Glide.with(this@MyFragment).load(R.drawable.imoji_10).into(myIcon)
            10 -> Glide.with(this@MyFragment).load(R.drawable.imoji_11).into(myIcon)
            11 -> Glide.with(this@MyFragment).load(R.drawable.imoji_12).into(myIcon)
        }


        btnAuth.setOnClickListener {// 인증 화면 이동
            val intent = Intent(context, AuthActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {// 로그아웃
            CoroutineScope(Dispatchers.IO).launch {
                viewModel?.logoutFireStore()?.join()

                launch {

                    Handler(Looper.getMainLooper()).post {
                        val dlg = CustomDialog(activity as MainActivity)
                        dlg.showLogoutDlg() // 로그아웃 다이얼로그
                    }

                    delay(2000)
                }.join()

                launch {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    (activity as MainActivity).finish()
                }.join()
            }
        }

        btnSecession.setOnClickListener {
            val dlg = CustomDialog(activity as MainActivity)
            dlg.showSecessionDlg() // 로그아웃 다이얼로그
        }


        btnApprule.setOnClickListener { intentWebView("https://sikdroid.tistory.com/21") }
    }


    private fun showSomething() { // UI State 정의
        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        viewModel.uiState.asLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is UiState.LoadingShow -> {
//                    showLoadingView()
//                    hideRecyclerView()
                }
                is UiState.Empty -> {
//                    hideLoadingView()
//                    showEmptyText()
                }
                is UiState.Success -> {
//                    hideLoadingView()
//                    showRecyclerView()
//                    adapter.submitList(it)
                }
                is UiState.Error -> {
//                    hideLoadingView()
//                    showErrorText(it.message.toString())
                }
                else -> {}
            }
        })
    }

    fun secession() {
        viewModel.secessionFireStore()

        viewModel.isSecession.observe(this, Observer {
            if (it) {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(activity as MainActivity, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                (activity as MainActivity).finish()
            }
        })
    }


    fun intentWebView(url : String) {
        val intent = Intent(context, WebviewActivity::class.java)
        intent.putExtra("url",url)

        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
//        scrollJobCreate()
    }

    override fun onPause() {
        super.onPause()
//        job.cancel()
    }

}