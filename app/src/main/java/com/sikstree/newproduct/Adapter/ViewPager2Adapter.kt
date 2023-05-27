package com.sikstree.newproduct.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sikstree.newproduct.R

class ViewPager2Adater(var list : ArrayList<String>,var context : Context)  : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return viewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        (holder as viewHolder).image.setBackgroundColor(list.get(position))
        if(position == 0) {
            Glide.with(context)
                .load(list.get(0))
                .centerCrop()
                .into((holder as viewHolder).image)
//            (holder as viewHolder).image.setImageResource(R.drawable.img_retro_place)
//            (holder as viewHolder).image.scaleType = ScaleType.CENTER_CROP
        } else if (position == 1) {
            Glide.with(context)
                .load(list.get(1))
                .centerCrop()
                .into((holder as viewHolder).image)
//            (holder as viewHolder).image.setImageResource(R.drawable.img_retro_place)
//            (holder as viewHolder).image.scaleType = ScaleType.CENTER_CROP
        } else if (position ==2) {
            Glide.with(context)
                .load(list.get(2))
                .centerCrop()
                .into((holder as viewHolder).image)

        }


        (holder as viewHolder).image.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    inner class viewHolder(var view : View) : RecyclerView.ViewHolder(view){
        var image : ImageView = view.findViewById(R.id.image)
    }
}
