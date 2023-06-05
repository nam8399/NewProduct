package com.sikstree.newproduct.viewModel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.sikstree.newproduct.Data.IconData
import com.sikstree.newproduct.Data.ProductData
import com.sikstree.newproduct.Data.ReviewData
import com.sikstree.newproduct.R
import kotlinx.coroutines.launch

class ReviewViewModel() : ViewModel() {
    private val title = "ReviewViewModel"

    var serverStatus = MutableLiveData<String>()
    var serverHostTxt = MutableLiveData<String>()

    var getEvent = MutableLiveData<Int>()

    private var firestore : FirebaseFirestore? = null

    init {
        serverStatus.value = ""
        serverHostTxt.value = ""

        firestore = FirebaseFirestore.getInstance()

        getEvent.value = 0
    }


    companion object {
        var listReviewAll = arrayListOf<ReviewData>()
    }


    fun initDetailData(
        name : String
    ) = viewModelScope.launch {
        Log.d(this@ReviewViewModel.title, "initDetailData")
        firestore?.collection("Review_Detail")?.whereEqualTo("review_title", name)
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우
                Log.d(this@ReviewViewModel.title, "성공")
                listReviewAll.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감)
                    listReviewAll.add(
                        ReviewData((document["review_profile"] as Long).toInt(),
                        document["review_id"] as String,
                        (document["review_imoji"] as Long).toInt(),
                        document["review_title"] as String,
                        document["review_comment"] as String,
                        document["review_date"] as String,
                        document["review_img"] as String,
                        document["review_img2"] as String,
                        document["review_img3"] as String)
                    )
                    Log.d(this@ReviewViewModel.title, "로그확인 : " + document["review_title"] as String)
                }

                getEvent.value = 1
            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(this@ReviewViewModel.title, "Error getting documents: $exception")
            }
    }

    fun getList(): ArrayList<ReviewData> {
        return listReviewAll
    }



    inner class ListAdapterGrid(): RecyclerView.Adapter<ListAdapterGrid.ViewHolder>() {

//    var datas = mutableListOf<ItemData>()

        var datas : ArrayList<IconData> = arrayListOf()

        init{
            var data = IconData("","","")
            for (i in 1..12)
                datas.add(data)

            notifyDataSetChanged()
        }



        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val itemUrl: String = ""
            private val itemImg: ImageView = itemView.findViewById(R.id.item_img)

            fun bind(item: IconData) {
                Glide
                    .with(itemView)
                    .load(R.drawable.ic_launcher_foreground)
                    .centerCrop()
                    .into(itemImg)


            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.list_grid_item, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(datas[position])

        }

        override fun getItemCount(): Int {
            return datas.size
        }

    }





}