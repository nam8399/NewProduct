package com.sikstree.newproduct.View.Fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import com.sikstree.newproduct.Data.UserUtil
import com.sikstree.newproduct.R
import com.sikstree.newproduct.View.Activity.MainActivity
import com.sikstree.newproduct.View.Activity.ReviewActivity
import com.sikstree.newproduct.View.Dialog.CustomLoadingDialog
import com.sikstree.newproduct.databinding.FragmentProductBinding
import com.sikstree.newproduct.viewModel.HomeViewModel
import com.sikstree.newproduct.viewModel.ProductViewModel

class ProductFragment() : Fragment() {
    private val title = "ProductFragment"

    lateinit var binding : FragmentProductBinding
    lateinit var reviewAdapter: ProductAdapter
    var datas = arrayListOf<ProductData>()
    lateinit var viewModel: ProductViewModel

    private lateinit var loadingAnimDialog : CustomLoadingDialog


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

    companion object {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        loadingAnimDialog = CustomLoadingDialog(activity as MainActivity)
        loadingAnimDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        showSomething()

        initData()

        initView()

        observeCategory()


    }

    override fun onResume() { // 리뷰 등록 같은 데이터 변경해야 할 FLAG 발생 시 화면 업데이트
        super.onResume()
        if (UserUtil.PRODUCT_VIEW_RESET) {
            initData()
            initView()
            observeCategory()

            with(binding) {
                imgAll.isSelected = true
                imgCookie.isSelected = false
                imgBread.isSelected = false
                imgRice.isSelected = false
                imgDrink.isSelected = false

                textAll.setTextColor(Color.WHITE)
                textRice.setTextColor(Color.parseColor("#676767"))
                textCookie.setTextColor(Color.parseColor("#676767"))
                textBread.setTextColor(Color.parseColor("#676767"))
                textDrink.setTextColor(Color.parseColor("#676767"))
            }

            UserUtil.PRODUCT_VIEW_RESET = false
        }
    }

    private fun showSomething() { // UI State 정의
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is UiState.LoadingShow -> {
                    handleLoadingState(true)
                }
                is UiState.LoadingDismiss -> {
                    handleLoadingState(true)
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

    private fun initView() { // 홈 화면 뷰 초기화
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initRecycler()

    }

    private fun initData() { // 화면에 보여질 데이터 생성
        handleLoadingState(true)
        viewModel.initData()

        viewModel.getEvent.observe(viewLifecycleOwner, Observer {
            when(it) {
                1 -> {
                    datas.clear()
                    for (i in 0..viewModel.getList().size-1) {
                        datas.add(viewModel.getList().get(i))
                    }
                    initRecycler()

                    handleLoadingState(false)
                }
            }
        })

    }


    private fun initRecycler() { // 전체 데이터 초기화
        reviewAdapter = ProductAdapter(activity as MainActivity)
        binding.reviewRecycler.adapter = reviewAdapter

        datas.apply {
            reviewAdapter.datas = datas
            reviewAdapter.notifyDataSetChanged()

        }

        reviewAdapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ProductData, pos : Int) {
                Log.d(title, "상품 선택 pos : " + pos)
                val intent = Intent(context, ReviewActivity::class.java)
                Log.d(title, "클릭 리뷰 데이터 - 전체" + data.review_title)
                intent.putExtra("title",data.review_title)
                startActivity(intent)
            }

        })
    }

    private fun initRecycler(datas : ArrayList<ProductData>) { // 카테고리 클릭 시 해당 카테고리 데이터만 초기화
        reviewAdapter = ProductAdapter(activity as MainActivity)
        binding.reviewRecycler.adapter = reviewAdapter

        if (datas.size == 0) {
            return
        }


        datas.apply {
            reviewAdapter.datas = datas
            reviewAdapter.notifyDataSetChanged()
        }

        reviewAdapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener{ // 카테고리 클릭 처리
            override fun onItemClick(v: View, data: ProductData, pos : Int) {
                Log.d(title, "상품 선택 pos : " + pos)

                val intent = Intent(context, ReviewActivity::class.java)
                Log.d(title, "클릭 리뷰 데이터 - 카테고리" + data.review_title)
                intent.putExtra("title",data.review_title)
                startActivity(intent)
            }

        })
    }

    private fun handleLoadingState(show : Boolean) = with(binding) {
        if (show) {
            loadingAnimDialog.show()
        } else {
            loadingAnimDialog.dismiss()
        }
    }


    private fun observeCategory() = with(viewModel) { // 상단 카테고리 버튼 클릭 시 처리
        onclickIdx.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> {
                    initRecycler()
                    with(binding) {
                        imgAll.isSelected = true
                        imgCookie.isSelected = false
                        imgBread.isSelected = false
                        imgRice.isSelected = false
                        imgDrink.isSelected = false

                        textAll.setTextColor(Color.WHITE)
                        textRice.setTextColor(Color.parseColor("#676767"))
                        textCookie.setTextColor(Color.parseColor("#676767"))
                        textBread.setTextColor(Color.parseColor("#676767"))
                        textDrink.setTextColor(Color.parseColor("#676767"))
                    }
                }
                1 -> {
                    selectItem(datas, 1)?.let { data -> initRecycler(data) }
                    with(binding) {
                        imgAll.isSelected = false
                        imgCookie.isSelected = false
                        imgBread.isSelected = false
                        imgRice.isSelected = true
                        imgDrink.isSelected = false

                        textAll.setTextColor(Color.parseColor("#676767"))
                        textRice.setTextColor(Color.WHITE)
                        textCookie.setTextColor(Color.parseColor("#676767"))
                        textBread.setTextColor(Color.parseColor("#676767"))
                        textDrink.setTextColor(Color.parseColor("#676767"))
                    }
                }
                2 -> {
                    selectItem(datas, 2)?.let { data -> initRecycler(data) }
                    with(binding) {
                        imgAll.isSelected = false
                        imgCookie.isSelected = true
                        imgBread.isSelected = false
                        imgRice.isSelected = false
                        imgDrink.isSelected = false

                        textAll.setTextColor(Color.parseColor("#676767"))
                        textRice.setTextColor(Color.parseColor("#676767"))
                        textCookie.setTextColor(Color.WHITE)
                        textBread.setTextColor(Color.parseColor("#676767"))
                        textDrink.setTextColor(Color.parseColor("#676767"))
                    }
                }
                3 -> {
                    selectItem(datas, 3)?.let { data -> initRecycler(data) }
                    with(binding) {
                        imgAll.isSelected = false
                        imgCookie.isSelected = false
                        imgBread.isSelected = true
                        imgRice.isSelected = false
                        imgDrink.isSelected = false

                        textAll.setTextColor(Color.parseColor("#676767"))
                        textCookie.setTextColor(Color.parseColor("#676767"))
                        textCookie.setTextColor(Color.parseColor("#676767"))
                        textBread.setTextColor(Color.WHITE)
                        textDrink.setTextColor(Color.parseColor("#676767"))
                    }
                }
                4 -> {
                    selectItem(datas, 4)?.let { data -> initRecycler(data) }
                    with(binding) {
                        imgAll.isSelected = false
                        imgCookie.isSelected = false
                        imgBread.isSelected = false
                        imgRice.isSelected = false
                        imgDrink.isSelected = true

                        textAll.setTextColor(Color.parseColor("#676767"))
                        textCookie.setTextColor(Color.parseColor("#676767"))
                        textCookie.setTextColor(Color.parseColor("#676767"))
                        textBread.setTextColor(Color.parseColor("#676767"))
                        textDrink.setTextColor(Color.WHITE)
                    }
                }
            }


        })
    }

}