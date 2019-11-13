package com.lescigognes.chatentempsreel.ui.chat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.lescigognes.chatentempsreel.App
import com.lescigognes.chatentempsreel.R
import com.lescigognes.chatentempsreel.`object`.Message
import com.lescigognes.chatentempsreel.`object`.RecyclerItemClickListener
import com.lescigognes.chatentempsreel.common.*
import com.lescigognes.chatentempsreel.network.GestionFirebaseChat
import com.lescigognes.chatentempsreel.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.uiThread
import java.lang.Exception

class ChatActivity : BaseActivity() {
    private var messageList: ArrayList<Message> = arrayListOf()
    private var adapter: MessageAdapter? = null
    private var messageEnAttenteSuppression: Message? = null
    private var indexSuppression: Int = 0
    private var enModif = false
    private var idModif = ""
    private var indexModif = 0
    private var ancienneValeurModif = ""
    private lateinit var gestionFirebaseChat: GestionFirebaseChat
    private var groupId:String="1"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        SimpleStorage.setString(this,"userId",
            App.currentUser?.id)

        ajouterActionAIntentFiltrer(ACTION_GET_MESSAGE_RESULT)
        ajouterActionAIntentFiltrer(ACTION_SEND_MESSAGE_RESULT)
        ajouterActionAIntentFiltrer(ACTION_CHANGED_MESSAGE_RESULT)
        ajouterActionAIntentFiltrer(ACTION_CHANGED_MY_MESSAGE_RESULT)
        ajouterActionAIntentFiltrer(ACTION_DELETE_MESSAGE_RESULT)
        ajouterActionAIntentFiltrer(ACTION_DELETE_MY_MESSAGE_RESULT)

        gestionFirebaseChat = GestionFirebaseChat(this)
        gestionFirebaseChat.getAllMessagesFromGroup(groupId)

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful)
                GestionFirebaseChat(this).sendTokenToFB(groupId,task.result!!.token)
        }

        FirebaseMessaging.getInstance().subscribeToTopic("messages")

        val linearLayoutManager = LinearLayoutManager(this@ChatActivity)
        linearLayoutManager.reverseLayout = true
        adapter = MessageAdapter(this, messageList)
        recyclerChat.adapter = adapter

        sendButton.setOnClickListener {
            //on vérifie que le message a envoyé n'est pas vide
            if (messageEdittext.text.toString().isNotEmpty()) {
                if (enModif)
                {
                    gestionFirebaseChat.changeMessage(messageEdittext.text.toString(),groupId, idModif)
                    messageList[indexModif].content = messageEdittext.text.toString()
                    recyclerChat.adapter?.notifyItemChanged(indexModif)
                    enModif = false
                }
                else
                    gestionFirebaseChat.uploadMessage(messageEdittext.text.toString(),groupId)
                messageEdittext.setText("")
                hideKeyboard(this)
            }
        }

        if (messageList.size == 0)
            noMessageTextview.visibility = View.VISIBLE
        else
            noMessageTextview.visibility = View.GONE

        recyclerChat.addOnItemTouchListener(
            RecyclerItemClickListener(this, recyclerChat, object : RecyclerItemClickListener.OnItemClickListener
            {
                override fun onItemClick(view: View, position: Int)
                {
                    recyclerChat.findViewHolderForAdapterPosition(position)?.itemView?.performClick()
                    (recyclerChat.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position,20)
                }

                override fun onLongItemClick(view: View?, position: Int)
                {
                    if (messageList[position].senderId == App.currentUser!!.id)
                        ActionMessageDialog().showDialog(this@ChatActivity, position,messageList[position].content.isNotEmpty())
                }
            })
        )

        addPhotoIcon.setOnClickListener { openGallery(this) }
    }

    fun modifierMessage(index: Int)
    {
        indexModif = index
        idModif = messageList[index].id
        messageEdittext.setText(messageList[index].content)
        ancienneValeurModif = messageList[index].content
        enModif = true
        messageEdittext.setSelection(messageEdittext.text.length)
    }

    fun deleteMessage(index: Int)
    {
        gestionFirebaseChat.deleteMessage(messageList[index].id,groupId)
        messageList.removeAt(index)
        adapter!!.removeItem(index)
    }

    override fun onDestroy()
    {
        super.onDestroy()
        gestionFirebaseChat.removeListener(groupId)
    }

    override fun doOnReceive(intent: Intent)
    {
        super.doOnReceive(intent)
        when (intent.action) {
            ACTION_SEND_MESSAGE_RESULT ->
            {
                if (!intent.getBooleanExtra(EXTRA_SUCCESS, false))
                    showLongSnackBar(getString(R.string.error_sending_message))
            }
            ACTION_GET_MESSAGE_RESULT ->
            {
                if (intent.getBooleanExtra(EXTRA_SUCCESS, false))
                {
                    doAsyncResult {
                        val messagerecu = Message(
                            intent.getStringExtra(EXTRA_MESSAGE_SENDERID), intent.getStringExtra(EXTRA_MESSAGE_CONTENT),
                            intent.getLongExtra(EXTRA_MESSAGE_DATETIME, System.currentTimeMillis()),
                            intent.getStringExtra(EXTRA_MESSAGE_ID), intent.getStringExtra(EXTRA_MESSAGE_SENDERULR),
                            intent.getStringExtra(EXTRA_MESSAGE_SENDERNAME), intent.getStringExtra(EXTRA_MESSAGE_IMAGE)
                        )
                        if (!messageList.contains(messagerecu))
                            messageList.add(messagerecu)
                        uiThread {
                            try { messageList = ArrayList(messageList.sortedBy { it.dateTime }) }
                            catch (e:Exception){e.printStackTrace()}
                            adapter!!.setItems(messageList)
                            (recyclerChat.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(messageList.size-1,0)
                            if (messageList.size == 0)
                                noMessageTextview.visibility = View.VISIBLE
                            else
                                noMessageTextview.visibility = View.GONE
                        }
                    }
                }
                else
                    showLongSnackBar(getString(R.string.error_retrieving_message))
            }
            ACTION_CHANGED_MY_MESSAGE_RESULT ->
            {
                if (!intent.getBooleanExtra(EXTRA_SUCCESS, false))
                {
                    showLongSnackBar(getString(R.string.error_sending_message))
                    messageList[indexModif].content = ancienneValeurModif
                }
            }
            ACTION_CHANGED_MESSAGE_RESULT ->
            {
                messageList.filter { it.id == intent.getStringExtra(EXTRA_MESSAGE_ID) }[0].content =
                    intent.getStringExtra(EXTRA_MESSAGE_CONTENT)
                adapter!!.changeItem(intent.getStringExtra(EXTRA_MESSAGE_ID),
                    intent.getStringExtra(EXTRA_MESSAGE_CONTENT))
            }
            ACTION_DELETE_MY_MESSAGE_RESULT ->
            {
                if (!intent.getBooleanExtra(EXTRA_SUCCESS, false))
                {
                    showLongSnackBar(getString(R.string.error_deleting_message))
                    messageList[indexSuppression] = messageEnAttenteSuppression!!
                    adapter!!.setItems(messageList)
                }

                if (messageList.size == 0)
                    noMessageTextview.visibility = View.VISIBLE
                else
                    noMessageTextview.visibility = View.GONE
            }
            ACTION_DELETE_MESSAGE_RESULT ->
            {
                if (intent.getStringExtra(EXTRA_MESSAGE_SENDERID) != App.currentUser!!.id)
                {
                    val index = messageList.indexOf(messageList.filter
                    { it.id == intent.getStringExtra(EXTRA_MESSAGE_ID) }[0])
                    messageList.removeAt(index)
                    adapter!!.removeItem(index)
                }

                if (messageList.size == 0)
                    noMessageTextview.visibility = View.VISIBLE
                else
                    noMessageTextview.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_RETOUR_IMAGE)
            if (resultCode == Activity.RESULT_OK && data != null)
                gestionFirebaseChat.uploadImage(data.data,groupId)
    }

    private fun openGallery(activity: Activity)
    {
        if (PermissionsHelper.checkForPermissionEnable(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            activity.startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), CODE_RETOUR_IMAGE)
        else
            PermissionsHelper.askForStoragePermission(activity)
    }
}
