package com.sikstree.newproduct.View.Fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.sikstree.newproduct.Adapter.ReviewAdapter
import com.sikstree.newproduct.Adapter.ViewPager2Adater
import com.sikstree.newproduct.Data.ReviewData
import com.sikstree.newproduct.Data.UiState
import com.sikstree.newproduct.R
import com.sikstree.newproduct.View.Activity.MainActivity
import com.sikstree.newproduct.databinding.FragmentHomeBinding
import com.sikstree.newproduct.databinding.FragmentProductBinding
import com.sikstree.newproduct.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class ProductFragment() : Fragment() {
    lateinit var binding : FragmentProductBinding
    lateinit var reviewAdapter: ReviewAdapter
    val datas = mutableListOf<ReviewData>()



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

    private fun initView() { // 홈 화면 뷰 초기화
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        onclick()
        initRecycler()

    }

    private fun onclick() {
        binding.imgCookie.setOnClickListener {
            binding.imgCookie.isSelected = binding.imgCookie.isSelected != true
        }

        binding.imgBread.setOnClickListener {
            binding.imgBread.isSelected = binding.imgBread.isSelected != true
        }

        binding.imgRice.setOnClickListener {
            binding.imgRice.isSelected = binding.imgRice.isSelected != true
        }

        binding.imgDrink.setOnClickListener {
            binding.imgDrink.isSelected = binding.imgDrink.isSelected != true
        }
    }


    private fun initRecycler() {
        reviewAdapter = ReviewAdapter(activity as MainActivity)
        binding.reviewRecycler.adapter = reviewAdapter


        datas.apply {
            add(ReviewData(1, 5, "연세우유 말차생크림빵", "최고에요!", "3,800",
                        "제품 설명을 해드리겠습니다.", "23개의 리뷰 모두 보기", "불타는삼각김밥", "지립니다."))
            add(ReviewData(2, 3, "연세우유 말차생크림빵", "최고에요!", "3,800",
                "제품 설명을 해드리겠습니다.", "23개의 리뷰 모두 보기", "불타는삼각김밥", "지립니다."))
            add(ReviewData(3, 4, "연세우유 말차생크림빵", "최고에요!", "3,800",
                "제품 설명을 해드리겠습니다.", "23개의 리뷰 모두 보기", "불타는삼각김밥", "지립니다."))


            reviewAdapter.datas = datas
            reviewAdapter.notifyDataSetChanged()

        }
    }




//    fun scrollJobCreate() { // auto Scroll을 위한 함수
//        job = lifecycleScope.launchWhenResumed {
//            delay(2000)
//            binding.viewpager2.setCurrentItem(++bannerPosition, true)
//            if (bannerPosition == 2) {
//                bannerPosition = -1
//            }
//        }
//    }

//    fun observerServerStatus() {
//        val loadingAnimDialog = CustomLoadingDialog(activity as MainActivity)
//        binding.viewModel?.apply {
//            event.observe(viewLifecycleOwner, EventObserver {
//                if (it) {
//                    binding.serverStatusOff.visibility = View.GONE
//                    binding.serverStatusOn.visibility = View.VISIBLE
//                } else {
//                    binding.serverStatusOff.visibility = View.VISIBLE
//                    binding.serverStatusOn.visibility = View.GONE
//                }
//
//            })
//
//            showDialog.observe(viewLifecycleOwner, Observer { // showDialog 변수를 observing 하면서 다이얼로그 show 및 dismiss
//                if (it) {
//                    loadingAnimDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                    loadingAnimDialog.show()
//                } else {
//                    loadingAnimDialog.dismiss()
//                }
//            })
//        }
//
//    }

    override fun onResume() {
        super.onResume()
//        scrollJobCreate()
    }

    override fun onPause() {
        super.onPause()
//        job.cancel()
    }

}