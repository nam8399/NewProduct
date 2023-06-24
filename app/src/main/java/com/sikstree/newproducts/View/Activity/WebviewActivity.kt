package com.sikstree.newproducts.View.Activity

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.sikstree.newproducts.databinding.ActivityWebviewBinding

class WebviewActivity : AppCompatActivity() {
    private var mBinding: ActivityWebviewBinding? = null
    private val binding get() = mBinding!!


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        mBinding = ActivityWebviewBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)


        binding.webview.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }

        val url = intent.getStringExtra("url")

        binding.webview.loadUrl(url.toString())

    }

    override fun onBackPressed() {
        if (binding.webview.canGoBack())
        {
            binding.webview.goBack()
        }
        else
        {
            finish()
        }
    }

}