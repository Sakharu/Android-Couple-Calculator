package com.lescigognes.chatentempsreel.common

import android.content.Context
import android.content.Intent

class BroadcastUtils {
    companion object {
        fun sendActionSuccessfull(context: Context, action: String, isSuccessful: Boolean) {
            val intent = Intent(action)
            intent.putExtra(EXTRA_SUCCESS, isSuccessful)
            context.sendBroadcast(intent)
        }

        fun sendGetMessageResult(context: Context, senderId: String, datetime: Long, content: String, id: String, senderUrl: String, senderName: String,image:String) {
            val intent = Intent(ACTION_GET_MESSAGE_RESULT)
            intent.apply {
                putExtra(EXTRA_SUCCESS, true)
                putExtra(EXTRA_MESSAGE_SENDERID, senderId)
                putExtra(EXTRA_MESSAGE_CONTENT, content)
                putExtra(EXTRA_MESSAGE_ID, id)
                putExtra(EXTRA_MESSAGE_DATETIME, datetime)
                putExtra(EXTRA_MESSAGE_SENDERNAME, senderName)
                putExtra(EXTRA_MESSAGE_SENDERULR, senderUrl)
                putExtra(EXTRA_MESSAGE_IMAGE, image)
            }

            context.sendBroadcast(intent)
        }

        fun sendGetMessageChanged(context: Context, action: String, contentMessage: String, messageKey: String) {
            val intent = Intent(action)
            intent.putExtra(EXTRA_MESSAGE_CONTENT, contentMessage)
            intent.putExtra(EXTRA_MESSAGE_ID, messageKey)
            context.sendBroadcast(intent)
        }

        fun sendMessageRemoved(context: Context, action: String, id: String, sendId: String) {
            val intent = Intent(action)
            intent.putExtra(EXTRA_MESSAGE_ID, id)
            intent.putExtra(EXTRA_MESSAGE_SENDERID, sendId)
            context.sendBroadcast(intent)
        }

    }
}