package com.lescigognes.chatentempsreel.ui.chat

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.lescigognes.chatentempsreel.R

class MessageMeHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal var messageText: TextView = itemView.findViewById(R.id.texteMessageMe)
    internal var global: ConstraintLayout = itemView.findViewById(R.id.layoutMessageItemMe)
    internal var viewSeparateur: View = itemView.findViewById(R.id.separateurViewMe)
    internal var dateSeparateur: TextView = itemView.findViewById(R.id.dateSeparateurMe)
    internal var authorName: TextView = itemView.findViewById(R.id.authorNameMessageMe)
    internal var imageMessage: ImageView = itemView.findViewById(R.id.imageMessageMe)
}