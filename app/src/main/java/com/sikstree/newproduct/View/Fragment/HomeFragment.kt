package com.sikstree.newproduct.View.Fragment

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
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.sikstree.newproduct.Adapter.ViewPager2Adater
import com.sikstree.newproduct.Data.HomeData
import com.sikstree.newproduct.Data.UiState
import com.sikstree.newproduct.R
import com.sikstree.newproduct.View.Activity.MainActivity
import com.sikstree.newproduct.databinding.FragmentHomeBinding
import com.sikstree.newproduct.viewModel.HomeViewModel

class HomeFragment() : Fragment() {
    lateinit var binding : FragmentHomeBinding
    var isSeverAdd : Boolean = false
//    lateinit var job : Job
    var bannerPosition : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.getName()

        viewModel.name.observe(viewLifecycleOwner) {
            binding.homeText.text = it + "님!\n신상품 한 번 잡숴보세요"
        }

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

    private fun initView() { // 홈 화면 뷰 초기화
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.getCU()
        viewModel.getGS()
        viewModel.get7()

        viewModel.getEvent.observe(viewLifecycleOwner, Observer {
            when(it) {
                1 -> initviewpagerCU(viewModel.getListCU())
                2 -> initviewpagerGS(viewModel.getListGS())
                3 -> initviewpager7(viewModel.getList7())
            }
        })


    }

    private fun initviewpager7(list: ArrayList<HomeData>) = with(binding) {
        var adapter = ViewPager2Adater(list,activity as MainActivity)

        viewpager27.offscreenPageLimit=3
        viewpager27.getChildAt(0).overScrollMode=View.OVER_SCROLL_NEVER
        viewpager27.adapter = adapter


        var transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(8))

        transform.addTransformer(ViewPager2.PageTransformer{ view: View, fl: Float ->
            var v = 1-Math.abs(fl)
            view.scaleY = 0.6f + v * 0.4f
        })

        viewpager27.setPageTransformer(transform)

        adapter.setItemClickListener(object : ViewPager2Adater.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
//                val intent = Intent(context, WebviewActivity::class.java)
//                if (position == 0) {
//                    intent.putExtra("url","https://www.youtube.com/watch?v=n1PkmOU7H2w")
//                } else if(position == 1) {
//                    intent.putExtra("url", "https://www.youtube.com/watch?v=hvydITbP-YE&t=95s")
//                } else if(position == 2) {
//                    intent.putExtra("url", "https://www.youtube.com/watch?v=n1PkmOU7H2w&t=2s")
//                }
//                startActivity(intent)
            }
        })

        viewpager27.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position : Int){
                super.onPageSelected(position)
                bannerPosition = position
                binding.sevenName.setText(list.get(position).name)
            }

        })
    }

    private fun initviewpagerGS(list: ArrayList<HomeData>) = with(binding) {
        var adapter = ViewPager2Adater(list,activity as MainActivity)

        viewpager2Gs.offscreenPageLimit=3
        viewpager2Gs.getChildAt(0).overScrollMode=View.OVER_SCROLL_NEVER
        viewpager2Gs.adapter = adapter


        var transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(8))

        transform.addTransformer(ViewPager2.PageTransformer{ view: View, fl: Float ->
            var v = 1-Math.abs(fl)
            view.scaleY = 0.6f + v * 0.4f
        })

        viewpager2Gs.setPageTransformer(transform)

        adapter.setItemClickListener(object : ViewPager2Adater.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
//                val intent = Intent(context, WebviewActivity::class.java)
//                if (position == 0) {
//                    intent.putExtra("url","https://www.youtube.com/watch?v=n1PkmOU7H2w")
//                } else if(position == 1) {
//                    intent.putExtra("url", "https://www.youtube.com/watch?v=hvydITbP-YE&t=95s")
//                } else if(position == 2) {
//                    intent.putExtra("url", "https://www.youtube.com/watch?v=n1PkmOU7H2w&t=2s")
//                }
//                startActivity(intent)
            }
        })

        viewpager2Gs.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position : Int){
                super.onPageSelected(position)
                bannerPosition = position
                binding.gsName.setText(list.get(position).name)

            }

        })
    }

    private fun initviewpagerCU(list: ArrayList<HomeData>) = with(binding) {
        var adapter = ViewPager2Adater(list,activity as MainActivity)

        viewpager2.offscreenPageLimit=3
        viewpager2.getChildAt(0).overScrollMode=View.OVER_SCROLL_NEVER
        viewpager2.adapter = adapter


        var transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(8))

        transform.addTransformer(ViewPager2.PageTransformer{ view: View, fl: Float ->
            var v = 1-Math.abs(fl)
            view.scaleY = 0.6f + v * 0.4f
        })

        viewpager2.setPageTransformer(transform)

        adapter.setItemClickListener(object : ViewPager2Adater.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
//                val intent = Intent(context, WebviewActivity::class.java)
//                if (position == 0) {
//                    intent.putExtra("url","https://www.youtube.com/watch?v=n1PkmOU7H2w")
//                } else if(position == 1) {
//                    intent.putExtra("url", "https://www.youtube.com/watch?v=hvydITbP-YE&t=95s")
//                } else if(position == 2) {
//                    intent.putExtra("url", "https://www.youtube.com/watch?v=n1PkmOU7H2w&t=2s")
//                }
//                startActivity(intent)
            }
        })

        viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position : Int){
                super.onPageSelected(position)
                bannerPosition = position
                binding.cuName.setText(list.get(position).name)

            }

        })
    }



    override fun onPause() {
        super.onPause()
//        job.cancel()
    }

}