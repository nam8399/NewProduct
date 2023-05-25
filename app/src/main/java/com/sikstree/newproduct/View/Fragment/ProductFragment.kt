package com.sikstree.newproduct.View.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.sikstree.newproduct.Adapter.ProductAdapter
import com.sikstree.newproduct.Data.ProductData
import com.sikstree.newproduct.Data.UiState
import com.sikstree.newproduct.R
import com.sikstree.newproduct.View.Activity.MainActivity
import com.sikstree.newproduct.View.Activity.ReviewActivity
import com.sikstree.newproduct.databinding.FragmentProductBinding
import com.sikstree.newproduct.viewModel.HomeViewModel
import com.sikstree.newproduct.viewModel.ProductViewModel

class ProductFragment() : Fragment() {
    lateinit var binding : FragmentProductBinding
    lateinit var reviewAdapter: ProductAdapter
    val datas = mutableListOf<ProductData>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSomething()

        initView()

        observeCategory()


    }

    private fun showSomething() { // UI State 정의
        val viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

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

    private fun initView() { // 홈 화면 뷰 초기화
        val viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        onclick()
        initRecycler()

    }

    private fun onclick() = with(binding) {
        imgCookie.setOnClickListener {
            imgCookie.isSelected = true
            imgBread.isSelected = false
            imgRice.isSelected = false
            imgDrink.isSelected = false
        }

        imgBread.setOnClickListener {
            imgCookie.isSelected = false
            imgBread.isSelected = true
            imgRice.isSelected = false
            imgDrink.isSelected = false
        }

        imgRice.setOnClickListener {
            imgCookie.isSelected = false
            imgBread.isSelected = false
            imgRice.isSelected = true
            imgDrink.isSelected = false
        }

        imgDrink.setOnClickListener {
            imgCookie.isSelected = false
            imgBread.isSelected = false
            imgRice.isSelected = false
            imgDrink.isSelected = true
        }

        imgBread.setOnClickListener {
            val intent = Intent(context, ReviewActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initRecycler() {
        reviewAdapter = ProductAdapter(activity as MainActivity)
        binding.reviewRecycler.adapter = reviewAdapter

        var review_img : Int

        review_img = R.drawable.banner_review

        datas.apply {
            add(ProductData(1, 5, 1,"연세우유 말차생크림빵","CU", "최고에요!", "3,800",
                         "23", "불타는삼각김밥", "지립니다.", review_img))
            add(ProductData(2, 3, 2,"연세우유 말차생크림빵", "GS25","최고에요!", "3,200",
                 "25", "불타는삼각김밥", "지립니다.", review_img))
            add(ProductData(3, 4, 3,"연세우유 말차생크림빵", "세븐일레븐","최고에요!", "4,200",
                 "16", "불타는삼각김밥", "지립니다.", review_img))


            reviewAdapter.datas = datas
            reviewAdapter.notifyDataSetChanged()

        }
    }

    private fun observeCategory() {

    }




    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

}