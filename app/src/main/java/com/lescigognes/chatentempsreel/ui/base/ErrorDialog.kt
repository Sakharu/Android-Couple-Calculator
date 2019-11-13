package com.lescigognes.chatentempsreel.ui.base

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.lescigognes.chatentempsreel.R

class ErrorDialog {
    var dialog: Dialog? = null
    fun showDialog(activity: Activity, msg: String) {
        dialog = Dialog(activity)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.dialog_error)

        val text = dialog!!.findViewById(R.id.text_dialog) as TextView
        text.text = msg

        val dialogButton = dialog!!.findViewById(R.id.btn_dialog) as Button
        dialogButton.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }

    fun isVisible(): Boolean {
        if (dialog == null)
            return false
        return dialog!!.isShowing
    }
}