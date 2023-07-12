package com.sikstree.newproducts.View.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sikstree.newproducts.Data.LoginState
import com.sikstree.newproducts.R
import com.sikstree.newproducts.View.Dialog.CustomDialog
import com.sikstree.newproducts.View.Dialog.CustomLoadingDialog
import com.sikstree.newproducts.databinding.ActivityLoginBinding
import com.sikstree.newproducts.viewModel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val viewModel : LoginViewModel by viewModels()

    private lateinit var loadingAnimDialog : CustomLoadingDialog

    private val TAG = this.javaClass.simpleName

    private lateinit var fetchJob: Job

    private var tokenId: String? = null  //Google Auth 인증에 성공하면 token 값으로 설정된다

    var backPressedTime : Long = 0

    init{
        instance = this
    }

    companion object{
        private var instance: LoginActivity? = null
        fun getInstance(): LoginActivity?{
            return instance
        }
    }


    /* GoogleSignInOptions */
    private val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    /* GoogleSignIn */
    private val googleSignIn by lazy {
        GoogleSignIn.getClient(this, googleSignInOptions)
    }

    /* FirebaseAuth */
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    /* Google Auth 로그인 결과 수신 */
    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "loginLauncher - result : $result")
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)?.let { account ->
                        Log.d(TAG, "loginLauncher - firebaseAuthWithGoogle : ${account.id}")
                        tokenId = account.idToken
                        viewModel.saveToken(
                            tokenId ?: throw java.lang.Exception()
                        )  //Loading 상태 이후 Login 상태로 변경
                    } ?: throw Exception()
                } catch (e: Exception) {
                    e.printStackTrace()
                    handleErrorState()  //Error 상태
                }
            } else {
                handleErrorState()  //Error 상태
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        loadingAnimDialog = CustomLoadingDialog(this@LoginActivity)
        loadingAnimDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        checkSecession() // 회원탈퇴 감지

        fetchJob = viewModel.fetchData(tokenId)

        observeData()
    }

    private fun checkSecession() {
        var secessionFlag = intent.getIntExtra("Secession", 0)

        if (secessionFlag == 1) {
            viewModel.login_check.value = 0
        }
    }


    private fun initViews() = with(binding) {
        viewModel?.getLogoutState()

        viewModel?.login_check?.observe(this@LoginActivity) {
            if (it == 1) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                viewModel?.uploadFirebase()

//                Snackbar.make(binding.root, "자동 로그인", Snackbar.LENGTH_SHORT).show()
                finish()
            }
        }

        loginGoogle.setOnClickListener {   //로그인 버튼 클릭 시
            handleLoadingState(true)
            val signInIntent: Intent = googleSignIn.signInIntent
            loginLauncher.launch(signInIntent)  //loginLauncher 로 결과 수신하여 처리
        }

        btnPrivacy.setOnClickListener {
            intentWebView("https://sikdroid.tistory.com/21")
        }

        btnTest.setOnClickListener {
            val dlg = CustomDialog(this@LoginActivity)
            dlg.showTestDlg() // 로그아웃 다이얼로그
        }
    }


    /* viewModel 을  관찰하여 상태 변화에 따라 처리 */
    private fun observeData() = viewModel.loginStateLiveData.observe(this) {
        Log.d(TAG, "observeData() - it : $it")
        when (it) {
            is LoginState.UnInitialized -> initViews()
            is LoginState.LoadingShow -> handleLoadingState(true)
            is LoginState.LoadingDismiss -> handleLoadingState(false)
            is LoginState.Login -> handleLoginState(it)
            is LoginState.Success -> handleSuccessState(it)
            is LoginState.Error -> handleErrorState()
        }
    }


    fun testLogin() { // 구글 심사 위한 테스트 계정 로그인
        Log.d(TAG, "test Login Start")
        val email = "namjs1012@gmail.com"
        val password = "slondy1012509"
        firebaseAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext, "테스트 로그인에 성공 하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel?.getLoginState()?.join()
                    }

                    val intent = Intent(this@LoginActivity, StartActivity::class.java)
                    intent.putExtra("uid",firebaseAuth.uid)
                    startActivity(intent)
                    handleLoadingState(false)
                    finish()
                } else {
                    Toast.makeText(
                        baseContext, "로그인에 실패 하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    /* Loading 상태인 경우 */
    private fun handleLoadingState(show : Boolean) = with(binding) {
        if (show) {
            loadingAnimDialog.show()
        } else {
            loadingAnimDialog.dismiss()
        }
    }


    /* Google Auth Login 상태인 경우 */
    private fun handleLoginState(state: LoginState.Login) = with(binding) {
//        progressBar.isVisible = true
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this@LoginActivity) { task ->
                if (task.isSuccessful) {  //Login 성공
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel?.getLoginState()?.join()
                    }


                    viewModel?.login_check?.observe(this@LoginActivity, Observer {
                        if (it == 2) {
                            Toast.makeText(this@LoginActivity, "구글 로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, StartActivity::class.java)
                            intent.putExtra("uid",firebaseAuth.uid)
                            startActivity(intent)
                            handleLoadingState(false)
                            finish()
                        }
                    })

                } else { //Login 실패
                    Toast.makeText(this@LoginActivity, "구글 로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show()
                    handleLoadingState(false)
                }
            }
    }


    /* Google Auth Login Success 상태인 경우 */
    private fun handleSuccessState(state: LoginState.Success) = with(binding) {
//        progressBar.isGone = true
        when (state) {
            is LoginState.Success.Registered -> {  //Google Auth 등록된 상태
                handleRegisteredState(state)  //Success.Registered 상태로 변경
            }
            is LoginState.Success.NotRegistered -> {  //Google Auth 미등록된 상태
//                Toast.makeText(this@LoginActivity, "NotRegistered", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /* Google Auth Login Registered 상태인 경우 */
    private fun handleRegisteredState(state: LoginState.Success.Registered) = with(binding) {

//        Glide.with(this@LoginActivity)
//            .load(state.profileImgeUri.toString())
//            .transition(
//                DrawableTransitionOptions.withCrossFade(
//                    DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
//                )
//            )
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .apply {
//                transforms(CenterCrop(), RoundedCorners(60f.fromDpToPx()))
//            }
//            .into(ivProfile)
    }


    /* Error 상태인 경우 */
    private fun handleErrorState() = with(binding) {
        Toast.makeText(this@LoginActivity, "Error State", Toast.LENGTH_SHORT).show()

        handleLoadingState(false)

    //        val intent = Intent(this@LoginActivity, StartActivity::class.java)
//        intent.putExtra("uid",firebaseAuth.uid)
//        startActivity(intent)
//        finish()
    }


    fun intentWebView(url : String) {
        val intent = Intent(this@LoginActivity, WebviewActivity::class.java)
        intent.putExtra("url",url)

        startActivity(intent)
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