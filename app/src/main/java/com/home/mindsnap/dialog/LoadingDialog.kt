package com.home.mindsnap.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.home.mindsnap.databinding.DialogLayoutBinding

class LoadingDialog(private val context : Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false) // 밖에 클릭해도 안사라지게
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(DialogLayoutBinding.inflate(LayoutInflater.from(context)).root)
    }
}