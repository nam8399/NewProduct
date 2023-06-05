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
import com.sikstree.newproduct.Data.ReviewData
import com.sikstree.newproduct.R

class ReviewAdapter(private val context: Context) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    var datas = mutableListOf<ReviewData>()
    var bannerPosition : Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.review_list,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val review_profile: ImageView = itemView.findViewById(R.id.review_profile)
        private val review_imoji: ImageView = itemView.findViewById(R.id.review_imoji)
        private val review_id: TextView = itemView.findViewById(R.id.review_id)
        private val review_grade_text: TextView = itemView.findViewById(R.id.review_grade_text)
        private val review_text: TextView = itemView.findViewById(R.id.review_text)
        private val review_date: TextView = itemView.findViewById(R.id.review_date)
        private val review_img: LinearLayout = itemView.findViewById(R.id.review_img)
        private val review_text_layout: LinearLayout = itemView.findViewById(R.id.review_text_layout)


        fun bind(item: ReviewData) {
            review_id.text = item.review_id
            review_text.text = item.review_comment
            review_date.text = item.review_date

            if (item.review_img.isEmpty() || "".equals(item.review_img)) {
                review_img.visibility = View.GONE
            }

            if ("".equals(item.review_comment) || item.review_comment.isEmpty()) {
                review_text_layout.visibility = View.GONE
            }


            when(item.review_imoji){
                1 -> {
                    Glide.with(itemView).load(R.drawable.icon_1).into(review_imoji)
                    review_grade_text.text = "별로에요.."
                }
                2 -> {
                    Glide.with(itemView).load(R.drawable.icon_2).into(review_imoji)
                    review_grade_text.text = "애매해요.."
                }
                3 -> {
                    Glide.with(itemView).load(R.drawable.icon_3).into(review_imoji)
                    review_grade_text.text = "보통이에요!"
                }
                4 -> {
                    Glide.with(itemView).load(R.drawable.icon_4).into(review_imoji)
                    review_grade_text.text = "좋아요!"
                }
                5 -> {
                    Glide.with(itemView).load(R.drawable.icon_5).into(review_imoji)
                    review_grade_text.text = "최고에요!"
                }
            }


            when(item.review_profile){
                1 -> Glide.with(itemView).load(R.drawable.imoji_1).into(review_profile)
                2 -> Glide.with(itemView).load(R.drawable.imoji_2).into(review_profile)
                3 -> Glide.with(itemView).load(R.drawable.imoji_3).into(review_profile)
                4 -> Glide.with(itemView).load(R.drawable.imoji_4).into(review_profile)
                5 -> Glide.with(itemView).load(R.drawable.imoji_5).into(review_profile)
                6 -> Glide.with(itemView).load(R.drawable.imoji_6).into(review_profile)
                7 -> Glide.with(itemView).load(R.drawable.imoji_7).into(review_profile)
                8 -> Glide.with(itemView).load(R.drawable.imoji_8).into(review_profile)
                9 -> Glide.with(itemView).load(R.drawable.imoji_9).into(review_profile)
                10 -> Glide.with(itemView).load(R.drawable.imoji_10).into(review_profile)
                11 -> Glide.with(itemView).load(R.drawable.imoji_11).into(review_profile)
                12 -> Glide.with(itemView).load(R.drawable.imoji_12).into(review_profile)
            }

        }
    }


}