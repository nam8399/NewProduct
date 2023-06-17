package com.sikstree.newproduct.View.Dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Looper
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.sikstree.newproduct.View.Activity.AuthActivity
import com.sikstree.newproduct.View.Fragment.MyFragment
import com.sikstree.newproduct.databinding.ActivityCustomDialogBinding
import java.util.logging.Handler

class CustomDialog(private val context : AppCompatActivity) {

    private lateinit var binding : ActivityCustomDialogBinding
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var listener : MyDialogGalleryListener

    fun show() {
        binding = ActivityCustomDialogBinding.inflate(context.layoutInflater)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        with(binding) {
            dlgAddLayout.visibility = View.VISIBLE
            dlgSaveLayout.visibility = View.INVISIBLE
            dlgLogoutLayout.visibility = View.INVISIBLE
            dlgSecessionLayout.visibility = View.INVISIBLE

            btnCamera.setOnClickListener() {
                AuthActivity.getInstance()?.CallCamera()
                dlg.dismiss()
            }

            btnGallery.setOnClickListener() {
                AuthActivity.getInstance()?.navigatePhotos()
                dlg.dismiss()
            }

            btnCancel.setOnClickListener() {
                dlg.dismiss()
            }

        }

        dlg.show()
    }


    fun showSaveDlg() {
        binding = ActivityCustomDialogBinding.inflate(context.layoutInflater)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(true)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        with(binding) {
            dlgAddLayout.visibility = View.INVISIBLE
            dlgSaveLayout.visibility = View.VISIBLE
            dlgLogoutLayout.visibility = View.INVISIBLE
            dlgSecessionLayout.visibility = View.INVISIBLE
        }

        dlg.show()

        android.os.Handler().postDelayed({
            dlg.dismiss()
        }, 3000)
    }

    fun showLogoutDlg() {
        binding = ActivityCustomDialogBinding.inflate(context.layoutInflater)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        with(binding) {
            dlgAddLayout.visibility = View.INVISIBLE
            dlgSaveLayout.visibility = View.INVISIBLE
            dlgLogoutLayout.visibility = View.VISIBLE
            dlgSecessionLayout.visibility = View.INVISIBLE
        }

        dlg.show()

    }

    fun dismissDlg() {
        dlg.dismiss()
    }



    fun setOnOKClickedListener(listener: (String) -> Unit) {
        this.listener = object: MyDialogGalleryListener {
            override fun onOKClicked(content: String) {
                listener(content)
            }
        }
    }


    interface MyDialogGalleryListener {
        fun onOKClicked(content : String)
    }



}
