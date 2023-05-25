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
        private val review_title_sub: TextView = itemView.findViewById(R.id.review_title_sub)
        private val review_great: TextView = itemView.findViewById(R.id.review_great)
        private val review_comment_count: TextView = itemView.findViewById(R.id.review_comment_count)
        private val review_price: TextView = itemView.findViewById(R.id.review_price)
        private val review_cm_id: TextView = itemView.findViewById(R.id.review_cm_id)
        private val review_cm_comment: TextView = itemView.findViewById(R.id.review_cm_comment)
        private val review_imoji: ImageView = itemView.findViewById(R.id.review_imoji)
        private val review_brand_img: ImageView = itemView.findViewById(R.id.review_brand_img)
        private val review_img: ImageView = itemView.findViewById(R.id.review_img)



        fun bind(item: ProductData) {
            review_title.text = item.review_title
            review_great.text = item.review_great
            review_comment_count.text = item.review_comment_count + "개의 리뷰 모두 보기"
            review_price.text = item.review_price + "원"
            review_cm_id.text = item.review_cm_id
            review_cm_comment.text = item.review_cm_comment
            review_title_sub.text = item.review_title_sub

            Glide.with(itemView).load(item.review_img).into(review_img)

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


        }



    }


}