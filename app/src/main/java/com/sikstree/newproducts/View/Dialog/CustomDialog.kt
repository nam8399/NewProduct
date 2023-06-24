package com.sikstree.newproducts.View.Dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sikstree.newproducts.View.Activity.AuthActivity
import com.sikstree.newproducts.View.Activity.LoginActivity
import com.sikstree.newproducts.View.Fragment.MyFragment
import com.sikstree.newproducts.databinding.ActivityCustomDialogBinding

class CustomDialog(private val context : AppCompatActivity) {

    private lateinit var binding : ActivityCustomDialogBinding
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var listener : MyDialogGalleryListener
    private lateinit var loadingAnimDialog : CustomLoadingDialog

    init {
        loadingAnimDialog = CustomLoadingDialog(context)
        loadingAnimDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

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
            dlgTestLayout.visibility = View.INVISIBLE

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
            dlgTestLayout.visibility = View.INVISIBLE
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
            dlgTestLayout.visibility = View.INVISIBLE
        }

        dlg.show()

    }

    fun showSecessionDlg() {
        binding = ActivityCustomDialogBinding.inflate(context.layoutInflater)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        with(binding) {
            dlgAddLayout.visibility = View.INVISIBLE
            dlgSaveLayout.visibility = View.INVISIBLE
            dlgLogoutLayout.visibility = View.INVISIBLE
            dlgSecessionLayout.visibility = View.VISIBLE
            dlgTestLayout.visibility = View.INVISIBLE
        }

        dlg.show()

        binding.btnX.setOnClickListener() { dlg.dismiss() }
        binding.btnOk.setOnClickListener {
            loadingAnimDialog.show()
            var myFragment = MyFragment.getInstance()
            myFragment?.secession()
        }


    }


    fun showTestDlg() {
        binding = ActivityCustomDialogBinding.inflate(context.layoutInflater)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        with(binding) {
            dlgAddLayout.visibility = View.INVISIBLE
            dlgSaveLayout.visibility = View.INVISIBLE
            dlgLogoutLayout.visibility = View.INVISIBLE
            dlgSecessionLayout.visibility = View.INVISIBLE
            dlgTestLayout.visibility = View.VISIBLE
        }

        dlg.show()


        with(binding) {
            btnTestX.setOnClickListener() { dlg.dismiss() }
            btnTestOk.setOnClickListener {
                if (!dlgTestEdit.text.toString().equals("1012509")) {
                    Toast.makeText(context, "비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                loadingAnimDialog.show()
                var loginActivity = LoginActivity.getInstance()
                loginActivity?.testLogin()
            }
        }


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
