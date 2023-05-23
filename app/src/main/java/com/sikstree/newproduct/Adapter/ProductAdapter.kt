package com.sikstree.newproduct.Adapter

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.sikstree.newproduct.Data.ProductData
import com.sikstree.newproduct.R

class ProductAdapter(private val context: Context) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var datas = mutableListOf<ProductData>()
    var bannerPosition : Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_list,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val review_title: TextView = itemView.findViewById(R.id.review_title)
        private val review_great: TextView = itemView.findViewById(R.id.review_great)
        private val review_text: TextView = itemView.findViewById(R.id.review_text)
        private val review_comment_count: TextView = itemView.findViewById(R.id.review_comment_count)
        private val review_price: TextView = itemView.findViewById(R.id.review_price)
        private val review_cm_id: TextView = itemView.findViewById(R.id.review_cm_id)
        private val review_cm_comment: TextView = itemView.findViewById(R.id.review_cm_comment)
        private val review_imoji: ImageView = itemView.findViewById(R.id.review_imoji)
        private val review_brand_img: ImageView = itemView.findViewById(R.id.review_brand_img)

        private val viewpager2: ViewPager2 = itemView.findViewById(R.id.viewpager2)
        private val review_indicators: LinearLayout = itemView.findViewById(R.id.indicators)


        fun bind(item: ProductData) {
            var builder_reviewtxt : SpannableStringBuilder

            if (item.review_text.length > 50) {
                var reviewtxt = ""
                reviewtxt = item.review_text.substring(0,50) + "... 더보기"
                builder_reviewtxt = SpannableStringBuilder(reviewtxt)

                val graySpan = ForegroundColorSpan(Color.parseColor("#979B9B"))
                builder_reviewtxt.setSpan(graySpan, 53, reviewtxt.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                review_text.text = builder_reviewtxt
            } else {
                review_text.text = item.review_text
            }



            review_title.text = item.review_title
            review_great.text = item.review_great
            review_comment_count.text = item.review_comment_count + "개의 리뷰 모두 보기"
            review_price.text = item.review_price + "원"
            review_cm_id.text = item.review_cm_id
            review_cm_comment.text = item.review_cm_comment

            when(item.review_imoji_idx){
                1 -> Glide.with(itemView).load(R.drawable.icon_1).into(review_imoji)
                2 -> Glide.with(itemView).load(R.drawable.icon_2).into(review_imoji)
                3 -> Glide.with(itemView).load(R.drawable.icon_3).into(review_imoji)
                4 -> Glide.with(itemView).load(R.drawable.icon_4).into(review_imoji)
                5 -> Glide.with(itemView).load(R.drawable.icon_5).into(review_imoji)
            }

            when(item.review_brand_idx){
                1 -> Glide.with(itemView).load(R.drawable.icon_cu).into(review_brand_img)
                2 -> Glide.with(itemView).load(R.drawable.icon_gs25).into(review_brand_img)
                3 -> Glide.with(itemView).load(R.drawable.icon_7eleven).into(review_brand_img)

            }


            initAdapter(item)

        }

        private fun initAdapter(item: ProductData) {
            var adapter = ViewPager2Adater(item.review_img_list, context)

//            viewpager2.offscreenPageLimit=3
            viewpager2.getChildAt(0).overScrollMode=View.OVER_SCROLL_NEVER
            viewpager2.adapter = adapter

            setupOnBoardingIndicators()
            setCurrentOnboardingIndicator(0)

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

                    setCurrentOnboardingIndicator(position)
                }

            })
        }

        private fun setupOnBoardingIndicators(){
            val indicators =
                arrayOfNulls<ImageView>(3)

            var layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
            )

            layoutParams.setMargins(8,0,8,0)

            for( i in indicators.indices){
                indicators[i] = ImageView(context)
                indicators[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.onboarding_indicator_inactive
                    ))

                indicators[i]?.layoutParams = layoutParams

                review_indicators?.addView(indicators[i])

            }
        }

        private fun setCurrentOnboardingIndicator( index : Int){ // 건축 강의 인디게이터 뷰 이미지 셋팅
            var childCount = review_indicators?.childCount
            for(i in  0 until childCount!!){
                var imageView = review_indicators?.getChildAt(i) as ImageView
                if(i==index){
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(context,
                        R.drawable.onboarding_indicator_active))
                }else{
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(context,
                        R.drawable.onboarding_indicator_inactive))
                }
            }
        }
    }


}