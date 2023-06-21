package com.sikstree.newproduct.View.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.sikstree.newproduct.R
import com.sikstree.newproduct.databinding.ActivityWebViewBinding

class WebviewActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)

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