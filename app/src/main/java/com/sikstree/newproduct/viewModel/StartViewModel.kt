package com.sikstree.newproduct.viewModel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.sikstree.newproduct.Data.IconData
import com.sikstree.newproduct.Data.LoginData
import com.sikstree.newproduct.R
import kotlinx.coroutines.launch

class StartViewModel() : ViewModel() {
    private val title = "LoginViewModel"

    var iconClickPosition = MutableLiveData<Int>()

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null
    var name_check = MutableLiveData<Int>() // 0 : 기본, 1 : 성공, 2 : 실패



    init {
        iconClickPosition.value = -1

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        name_check.value = 0

    }


    fun getNameCheck(name : String) = viewModelScope.launch {
        firestore?.collection("UserID")
            ?.get()      // 문서 가져오기
            ?.addOnSuccessListener { result ->
                // 성공할 경우
                Log.d(title, "result size : " + result.size())
                if (result.size() == 0) {
                    name_check.value = 1
                }

                for (document in result) {  // 가져온 문서들은 result에 들어감
                    if (name?.equals(document["name"] as String)!!) {
                        Log.d(title, "Name check - " + document["name"] as String)
                        name_check.value = 2
                        break
                    }
                }
                name_check.value = 1
            }
            ?.addOnFailureListener { exception ->
                // 실패할 경우
                Log.w(title, "Error getting documents: $exception")
            }
    }





    fun uploadFirebase(name : String) {
        var loginData = LoginData()
        loginData.uid = auth?.currentUser?.uid
        loginData.name = name
        loginData.imoji = iconClickPosition.value

        firestore?.collection("UserID")?.document(auth!!.currentUser!!.uid)?.set(loginData)
//        Toast.makeText(this,"저장완료",Toast.LENGTH_SHORT).show()
        Log.d(title, "파이어베이스 저장완료 - " + auth!!.uid.toString())
    }


    inner class ListAdapterGrid(iconList : ArrayList<Int>): RecyclerView.Adapter<ListAdapterGrid.ViewHolder>() {

//    var datas = mutableListOf<ItemData>()

        var datas = iconList

//        init{
//            var data = IconData("")
//            for (i in 1..12)
//                datas.add(i)
//
//            notifyDataSetChanged()
//        }



        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

//            private val itemTitle: TextView = itemView.findViewById(R.id.item_title)
            private val itemUrl: String = ""
            private val itemImg: ImageView = itemView.findViewById(R.id.item_img)
            private val layoutListItem: ConstraintLayout = itemView.findViewById(R.id.layoutListItem)


            fun bind(position : Int) {

                layoutListItem.setOnClickListener {
                    Log.d(title, "position : " + position)
                    iconClickPosition.value = position
                }
                itemImg.setImageResource(datas.get(position))
                itemImg.isSelected = iconClickPosition.value == position
            }

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.list_grid_item, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(position)


//        holder.layout.layoutListItem.setOnClickListener {
//            Toast.makeText(holder.layout.context, "${list[position]} Click!", Toast.LENGTH_SHORT).show()
//        }
        }

        override fun getItemCount(): Int {
            return datas.size
        }

    }





}