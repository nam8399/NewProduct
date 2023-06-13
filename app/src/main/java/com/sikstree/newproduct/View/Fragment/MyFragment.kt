package com.sikstree.newproduct.View.Fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.sikstree.newproduct.Data.UiState
import com.sikstree.newproduct.R
import com.sikstree.newproduct.View.Activity.AuthActivity
import com.sikstree.newproduct.View.Activity.MainActivity
import com.sikstree.newproduct.View.Dialog.CustomDialog
import com.sikstree.newproduct.databinding.FragmentMyBinding
import com.sikstree.newproduct.viewModel.HomeViewModel
import java.io.File
import java.io.FileOutputStream

class MyFragment() : Fragment() {
    lateinit var binding : FragmentMyBinding
    var isSeverAdd : Boolean = false

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




        return binding.root
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        showSomething()



    }

    private fun initView() = with(binding) {

        btnAuth.setOnClickListener() {
            val intent = Intent(context, AuthActivity::class.java)
            startActivity(intent)
        }
    }


    private fun showSomething() { // UI State 정의
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        viewModel.uiState.asLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is UiState.Loading -> {
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
            }
        })
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