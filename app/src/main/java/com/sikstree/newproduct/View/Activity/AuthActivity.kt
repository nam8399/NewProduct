package com.sikstree.newproduct.View.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.sikstree.newproduct.R
import com.sikstree.newproduct.View.Dialog.CustomDialog
import com.sikstree.newproduct.databinding.ActivityAuthBinding
import com.sikstree.newproduct.viewModel.MyViewModel
import java.io.File
import java.io.FileOutputStream

class AuthActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAuthBinding
    private val viewModel : MyViewModel by viewModels()
    private val title = "AuthActivity"

    var backPressedTime : Long = 0
    var checkImgAdd = false


    init{
        instance = this
    }

    companion object{
        private var instance: AuthActivity? = null
        fun getInstance(): AuthActivity?{
            return instance
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initView()


    }

    private fun initView() = with(binding){
        imgCertification.setOnClickListener() {
//            navigatePhotos()
            val dlg = CustomDialog(this@AuthActivity)
            dlg.show()
        }

        btnAddCert.setOnClickListener() {
            if (!checkImgAdd) {
                Toast.makeText(this@AuthActivity, "사진을 등록해주세요.", Toast.LENGTH_SHORT).show()
            } else if(imageExternalSave(this@AuthActivity, viewToBitmap(frameCertification), "")) {
                val dlg = CustomDialog(this@AuthActivity)
                dlg.showSaveDlg()
            }
        }

        authBack.setOnClickListener() {
            finish()
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




    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
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
//                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,currentImgUrl)


                    with(binding) {
//                        imgCertification.setImageBitmap(bitmap)
                        Glide.with(this@AuthActivity).load(currentImgUrl).into(imgCertification)
                        checkImgAdd = true
//                        btnAddCert.isEnabled = true
//                        btnAddCert.isSelected = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            3000 -> {
                if(data?.extras?.get("data") != null){
                    val img = data?.extras?.get("data") as Bitmap
                    with(binding) {
//                        imgCertification.setImageBitmap(img)
                        Glide.with(this@AuthActivity).load(img).into(imgCertification)
                        checkImgAdd = true
//                        btnAddCert.isEnabled = true
//                        btnAddCert.isSelected = true
                    }
                }
            }
        }
    }


    override fun onBackPressed() {
        finish()
    }


}