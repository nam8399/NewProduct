package com.sikstree.newproduct.View.Dialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sikstree.newproduct.R

class CustomLoadingDialog(context: Context): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_loading_dialog)
    }
}