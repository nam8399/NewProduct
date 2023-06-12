package com.sikstree.newproduct.View.Fragment

import android.content.Intent
import android.graphics.Color
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
import com.sikstree.newproduct.databinding.FragmentProductBinding
import com.sikstree.newproduct.viewModel.HomeViewModel
import com.sikstree.newproduct.viewModel.ProductViewModel

class ProductFragment() : Fragment() {
    private val title = "ProductFragment"

    lateinit var binding : FragmentProductBinding
    lateinit var reviewAdapter: ProductAdapter
    var datas = arrayListOf<ProductData>()
    lateinit var viewModel: ProductViewModel



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

        showSomething()

        initData()

        initView()

        observeCategory()


    }

    override fun onResume() {
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
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

//        onclick()
        initRecycler()

    }

    private fun initData() {
        viewModel.initData()

        viewModel.getEvent.observe(viewLifecycleOwner, Observer {
            when(it) {
                1 -> {
                    datas.clear()
                    for (i in 0..viewModel.getList().size-1) {
                        datas.add(viewModel.getList().get(i))
                    }
                    initRecycler()
                }
            }
        })


    }


    private fun initRecycler() {
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
                Log.d(title, "클릭 리뷰 데이터" + data.review_title)
                intent.putExtra("title",data.review_title)
                startActivity(intent)
            }

        })

    }

    private fun initRecycler(datas : ArrayList<ProductData>) {
        reviewAdapter = ProductAdapter(activity as MainActivity)
        binding.reviewRecycler.adapter = reviewAdapter

        if (datas.size == 0) {
            return
        }


        datas.apply {
            reviewAdapter.datas = datas
            reviewAdapter.notifyDataSetChanged()
        }

        reviewAdapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ProductData, pos : Int) {
                Log.d(title, "상품 선택 pos : " + pos)

                val intent = Intent(context, ReviewActivity::class.java)
                Log.d(title, "클릭 리뷰 데이터2" + data.review_title)
                intent.putExtra("title",data.review_title)
                startActivity(intent)
            }

        })
    }

    private fun observeCategory() = with(viewModel) {
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