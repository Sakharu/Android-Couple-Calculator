package com.lescigognes.chatentempsreel.ui.chat

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.lescigognes.chatentempsreel.R
import de.hdodenhof.circleimageview.CircleImageView

class MessageHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    internal var global: ConstraintLayout = itemView.findViewById(R.id.layoutMessage)
    internal var messageText: TextView = itemView.findViewById(R.id.textMessage)
    internal var imageSender: CircleImageView = itemView.findViewById(R.id.senderImage)
    internal var viewSeparateur: View = itemView.findViewById(R.id.separateurView)
    internal var dateSeparateur: TextView = itemView.findViewById(R.id.dateSeparateur)
    internal var authorName: TextView = itemView.findViewById(R.id.authorNameMessage)
    internal var imageMessage: ImageView = itemView.findViewById(R.id.imageMessage)

}
