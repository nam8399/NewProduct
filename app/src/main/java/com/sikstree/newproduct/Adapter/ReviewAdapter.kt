package com.sikstree.newproduct.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sikstree.newproduct.Data.ReviewData
import com.sikstree.newproduct.R

class ReviewAdapter(private val context: Context) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    var datas = mutableListOf<ReviewData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.review_list,parent,false)
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

        fun bind(item: ReviewData) {
            review_title.text = item.review_title
            review_great.text = item.review_great
            review_text.text = item.review_text
            review_comment_count.text = item.review_comment_count
            review_price.text = item.review_price.toString()
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


        }
    }


}