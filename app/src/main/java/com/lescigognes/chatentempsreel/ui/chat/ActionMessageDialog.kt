package com.lescigognes.chatentempsreel.ui.chat

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import com.lescigognes.chatentempsreel.R

class ActionMessageDialog
{
    var dialog: Dialog? = null
    fun showDialog(activity: Activity, index: Int,isMessage:Boolean)
    {
        dialog = Dialog(activity)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_action_message)

        val modify = dialog!!.findViewById(R.id.layoutmodify) as ConstraintLayout
        val delete = dialog!!.findViewById(R.id.layoutdelete) as ConstraintLayout
        val viewModify = dialog!!.findViewById(R.id.view5) as View

        if (!isMessage)
        {
            viewModify.visibility = View.GONE
            modify.visibility = View.GONE
        }
        else
        {
            modify.setOnClickListener {
                (activity as ChatActivity).modifierMessage(index)
                dialog!!.dismiss()
            }
        }


        delete.setOnClickListener {
            (activity as ChatActivity).deleteMessage(index)
            dialog!!.dismiss()
        }
        dialog!!.show()
    }
}