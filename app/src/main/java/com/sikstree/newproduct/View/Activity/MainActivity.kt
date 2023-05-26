package com.sikstree.newproduct.View.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.sikstree.newproduct.R
import com.sikstree.newproduct.View.Fragment.AuthFragment
import com.sikstree.newproduct.View.Fragment.HomeFragment
import com.sikstree.newproduct.View.Fragment.ProductFragment
import com.sikstree.newproduct.databinding.ActivityMainBinding
import com.sikstree.newproduct.viewModel.MainViewModel
import com.sikstree.newproduct.viewModel.MainViewModel.Companion.TAG_AUTH
import com.sikstree.newproduct.viewModel.MainViewModel.Companion.TAG_HOME
import com.sikstree.newproduct.viewModel.MainViewModel.Companion.TAG_PRODUCT

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()
    private val fragmentManager : FragmentManager = supportFragmentManager
    var backPressedTime : Long = 0
    val MY_PERMISSION_ACCESS_ALL = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.setFragment(TAG_HOME, HomeFragment(), fragmentManager)

        initFirebase()
        updateResult()
        onClick()


        if(ContextCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

            Toast.makeText(this@MainActivity, "권한 허용 완료", Toast.LENGTH_SHORT).show()
        }

        // 권한이 없을 때 권한을 요구함
        else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

//        if(requestCode == 1){
//            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Snackbar.make(binding.root, "위치 권한이 동의 되었습니다.", Snackbar.LENGTH_SHORT).show()
//            }
//            else{
//                Snackbar.make(binding.root, "권한에 동의하지 않을 경우 이용할 수 없습니다.", Snackbar.LENGTH_SHORT).show()
//            }
//        }
    }



    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        updateResult(true)
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                binding.tokenId.text = task.result
                Log.d("토큰", task.result)
            }
        }
    }

    private fun updateResult(isNewIntent: Boolean = false) {
        //true -> notification 으로 갱신된 것
        //false -> 아이콘 클릭으로 앱이 실행된 것
//        binding.tokenId.text = (intent.getStringExtra("notificationType") ?: "앱 런처") + if (isNewIntent) {
//            "(으)로 갱신했습니다."
//        } else {
//            "(으)로 실행했습니다."
//        }
    }

    fun onClick() {
        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> viewModel.setFragment(TAG_HOME, HomeFragment(), fragmentManager)
                R.id.productFragment -> viewModel.setFragment(TAG_PRODUCT, ProductFragment(), fragmentManager)
                R.id.authFragment-> viewModel.setFragment(TAG_AUTH, AuthFragment(), fragmentManager)
            }
            true
        }

    }





    override fun onBackPressed() {
        //2.5초이내에 한 번 더 뒤로가기 클릭 시
        if (System.currentTimeMillis() - backPressedTime < 2500) {
            super.onBackPressed()
            return
        }
        Toast.makeText(this, "한번 더 클릭 시 홈으로 이동됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }


}