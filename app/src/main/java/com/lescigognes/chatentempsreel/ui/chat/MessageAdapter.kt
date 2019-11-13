package com.lescigognes.chatentempsreel.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.lescigognes.chatentempsreel.App
import com.lescigognes.chatentempsreel.`object`.Message
import com.lescigognes.chatentempsreel.R
import com.lescigognes.chatentempsreel.common.DATETIME_CHAT
import com.lescigognes.chatentempsreel.common.Utils
import com.squareup.picasso.Picasso

class MessageAdapter internal constructor(private var context: Context, private var listeMessage: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val threeHoursInMilliseconds = 10800000

    override fun getItemViewType(position: Int): Int
    {
        if (listeMessage[position].senderId == App.currentUser!!.id)
            return 1
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        return when (viewType)
        {
            0 -> MessageHolder(mInflater.inflate(R.layout.item_message, parent, false))
            else -> MessageMeHolder(mInflater.inflate(R.layout.item_message_me, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0)
        {
            holder as MessageHolder
            if (listeMessage[position].content.isNotEmpty())
            {
                holder.messageText.text = listeMessage[position].content
                holder.messageText.visibility = View.VISIBLE
                holder.imageMessage.visibility = View.GONE
                Utils.changeConstraintProgrammatically(holder.global,holder.authorName.id,ConstraintSet.TOP,holder.messageText.id,ConstraintSet.BOTTOM,4)
            }
            else
            {
                if (listeMessage[position].image.isNotEmpty())
                {
                    holder.messageText.visibility = View.VISIBLE
                    holder.imageMessage.visibility = View.VISIBLE
                    Picasso.get().load(listeMessage[position].image).into(holder.imageMessage)
                }
                Utils.changeConstraintProgrammatically(holder.global,holder.authorName.id,ConstraintSet.TOP,holder.imageMessage.id,ConstraintSet.BOTTOM,4)
            }

            holder.authorName.text = context.getString(R.string.detailsMessage, listeMessage[position].senderName, DATETIME_CHAT.format(listeMessage[position].dateTime))

            if (listeMessage[position].senderImage.isNotEmpty())
                Picasso.get().load(listeMessage[position].senderImage).error(R.drawable.placeholder_group).into(holder.imageSender)

            if (position == 0 || (listeMessage[position].dateTime - listeMessage[position - 1].dateTime) > threeHoursInMilliseconds) {
                holder.viewSeparateur.visibility = View.VISIBLE
                holder.dateSeparateur.text = DATETIME_CHAT.format(listeMessage[position].dateTime)
                holder.dateSeparateur.visibility = View.VISIBLE
            }
            else
            {
                holder.viewSeparateur.visibility = View.GONE
                holder.dateSeparateur.visibility = View.GONE
            }

            holder.global.setOnClickListener {
                if (holder.authorName.visibility == View.VISIBLE)
                    holder.authorName.visibility = View.GONE
                else
                    holder.authorName.visibility = View.VISIBLE
            }
        }
        else
        {
            holder as MessageMeHolder
            if (listeMessage[position].content.isNotEmpty())
            {
                holder.messageText.text = listeMessage[position].content
                holder.messageText.visibility = View.VISIBLE
                holder.imageMessage.visibility = View.GONE
                Utils.changeConstraintProgrammatically(holder.global,holder.authorName.id,ConstraintSet.TOP,holder.messageText.id,ConstraintSet.BOTTOM,4)
            }
            else
            {
                if (listeMessage[position].image.isNotEmpty())
                {
                    holder.messageText.visibility = View.VISIBLE
                    holder.imageMessage.visibility = View.VISIBLE
                    Picasso.get().load(listeMessage[position].image).into(holder.imageMessage)
                }
                Utils.changeConstraintProgrammatically(holder.global,holder.authorName.id,ConstraintSet.TOP,holder.imageMessage.id,ConstraintSet.BOTTOM,4)
            }
            holder.authorName.text = context.getString(R.string.detailsMessage, listeMessage[position].senderName, DATETIME_CHAT.format(listeMessage[position].dateTime))

            if (position == 0 || (listeMessage[position].dateTime - listeMessage[position - 1].dateTime) > threeHoursInMilliseconds) {
                holder.viewSeparateur.visibility = View.VISIBLE
                holder.dateSeparateur.text = DATETIME_CHAT.format(listeMessage[position].dateTime)
                holder.dateSeparateur.visibility = View.VISIBLE
            }
            else
            {
                holder.viewSeparateur.visibility = View.GONE
                holder.dateSeparateur.visibility = View.GONE
            }

            holder.global.setOnClickListener {
                if (holder.authorName.visibility == View.VISIBLE)
                    holder.authorName.visibility = View.GONE
                else
                    holder.authorName.visibility = View.VISIBLE

            }
        }
    }

    override fun getItemCount(): Int = listeMessage.size

    fun setItems(listeMessage: ArrayList<Message>) {
        this.listeMessage.clear()
        this.listeMessage.addAll(listeMessage)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        this.listeMessage.removeAt(position)
        notifyDataSetChanged()
    }

    fun changeItem(id: String, message: String) {
        listeMessage.filter { it.id == id }[0].content = message
        notifyDataSetChanged()
    }


}