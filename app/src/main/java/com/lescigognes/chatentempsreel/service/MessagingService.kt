package com.lescigognes.chatentempsreel.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lescigognes.chatentempsreel.R
import com.lescigognes.chatentempsreel.common.SimpleStorage
import com.lescigognes.chatentempsreel.network.GestionFirebaseChat

class MessagingService :  FirebaseMessagingService()
{
    override fun onMessageReceived(remoteMessage: RemoteMessage?)
    {

        remoteMessage?.data?.isNotEmpty()?.let {
            Log.d("notifRecue",remoteMessage.data.toString())
            if (remoteMessage.data!=null && remoteMessage.data!!.containsKey("senderId") && remoteMessage.data!!["senderId"] != SimpleStorage.getString(this,"userId","1"))
                sendNotif(remoteMessage.data)
        }
        remoteMessage?.notification?.let {}
    }


    override fun onNewToken(p0: String?)
    {
        super.onNewToken(p0)
        if (p0 != null)
            GestionFirebaseChat(this).sendTokenToFB("1",p0)
    }


    private fun sendNotif(data : Map<String,String>)
    {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var text =data["body"]
        if (text.isNullOrEmpty())
            text = "${data["senderName"]} a envoyé une image"
        val builder = NotificationCompat.Builder(this, "channel")
        builder.setContentTitle(data["title"])
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_send_black_24dp)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setColorized(true)
            .setAutoCancel(true)
            .setOngoing(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val mChannel = NotificationChannel( "channel", getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH)
            mChannel.description = data["title"]
            mChannel.enableLights(true)
            mChannel.lightColor = Color.BLUE
            mChannel.setShowBadge(true)
            mChannel.vibrationPattern = longArrayOf(100, 250)
            mNotificationManager.createNotificationChannel(mChannel)
        }
        else
        {
            builder.setContentTitle(data["title"])
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.BLUE)
                .setVibrate(longArrayOf(100, 250))
                .setLights(Color.BLUE, 500, 5000)
        }
        mNotificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }


}

