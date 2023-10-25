package com.mwbtech.dealer_register.Utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.TextView
import com.mwbtech.dealer_register.R


fun Context.confirm(message:String, positiveButton:String, negativeButton:String, positiveListener: View.OnClickListener?,negativeListener: View.OnClickListener?):Dialog{
    val mDialog = Dialog(this)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    mDialog.setContentView(R.layout.logout)
    mDialog.setCancelable(false)
    mDialog.setCanceledOnTouchOutside(false);
    mDialog.create()
    mDialog.findViewById<TextView>(R.id.message).apply {
        text = message
    }
    mDialog.findViewById<TextView>(R.id.tvLogoutOk).apply {
        text = positiveButton
        setOnClickListener { view->
            run {
                mDialog.dismiss()
                positiveListener?.onClick(view)
            }
        }
    }
    mDialog.findViewById<TextView>(R.id.tvLogoutCancel).apply {
        text = negativeButton
        setOnClickListener { view->
            run {
                mDialog.dismiss()
                negativeListener?.onClick(view)
            }
        }
    }
    mDialog.show()
    return mDialog
}