package com.sikstree.newproduct.View.Fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.sikstree.newproduct.Adapter.ViewPager2Adater
import com.sikstree.newproduct.Data.UiState
import com.sikstree.newproduct.R
import com.sikstree.newproduct.View.Activity.MainActivity
import com.sikstree.newproduct.View.Dialog.CustomDialog
import com.sikstree.newproduct.databinding.FragmentAuthBinding
import com.sikstree.newproduct.viewModel.HomeViewModel
import java.io.File
import java.io.FileOutputStream

class AuthFragment() : Fragment() {
    lateinit var binding : FragmentAuthBinding
    var isSeverAdd : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    init{
        instance = this
    }

    companion object{
        private var instance: AuthFragment? = null
        fun getInstance():AuthFragment?{
            return instance
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false)


        binding.imgCertification.setOnClickListener() {
//            navigatePhotos()
            val dlg = CustomDialog(activity as MainActivity)
            dlg.show()
        }

        binding.btnAddCert.setOnClickListener() {
            if(imageExternalSave(activity as MainActivity, viewToBitmap(binding.frameCertification), "")) {
                Toast.makeText(context,"사진저장 성공", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSomething()



    }

    fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,2000)
    }

    fun CallCamera(){
        val itt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(itt, 3000)
    }


    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) {
//            Toast.makeText(this,"잘못된 접근입니다",Toast.LENGTH_SHORT).show()
            return
        }
        when (requestCode) {
            2000 -> {
                var currentImgUrl : Uri? = data?.data

                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,currentImgUrl)


                    with(binding) {
                        imgCertification.setImageBitmap(bitmap)
                        btnAddCert.isEnabled = true
                        btnAddCert.isSelected = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            3000 -> {
                if(data?.extras?.get("data") != null){
                    val img = data?.extras?.get("data") as Bitmap
                    binding.imgCertification.setImageBitmap(img)
                }
            }
        }
    }



    fun imageExternalSave(context: Context, bitmap: Bitmap, path: String): Boolean {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {

            val rootPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString()
            val dirName = "/" + path
            val fileName = System.currentTimeMillis().toString() + ".png"
            val savePath = File(rootPath + dirName)
            savePath.mkdirs()

            val file = File(savePath, fileName)
            if (file.exists()) file.delete()

            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()

                //갤러리 갱신
                context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())
                    )
                )

                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }



    private fun showSomething() { // UI State 정의
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

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





    override fun onResume() {
        super.onResume()
//        scrollJobCreate()
    }

    override fun onPause() {
        super.onPause()
//        job.cancel()
    }

}