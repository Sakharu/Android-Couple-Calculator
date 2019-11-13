package com.lescigognes.chatentempsreel.network

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lescigognes.chatentempsreel.App
import com.lescigognes.chatentempsreel.`object`.Message
import com.lescigognes.chatentempsreel.common.*
import com.lescigognes.chatentempsreel.R

class GestionFirebaseChat(private var context: Context)
{
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var messageListener : ChildEventListener

    fun uploadMessage(content: String="",groupId:String,adresseImage: String="")
    {
        //la methode push permet de récupérer un uid que l'on va récupérer pour stocker dans le message
        //le champs_chat correspond à "chat"
        val key = database.child(CHAMPS_CHAT).child(groupId).child("messages").push().key.toString()
        val message = if (content.isNotEmpty())
            Message(App.currentUser!!.id, content, System.currentTimeMillis(), key, App.currentUser!!.image!!, App.currentUser!!.nom)
        else
            Message(App.currentUser!!.id, "", System.currentTimeMillis(), key, App.currentUser!!.image!!, App.currentUser!!.nom,adresseImage)
        database.child(CHAMPS_CHAT).child(groupId).child("messages").child(key).setValue(message)
    }

    fun changeMessage(content: String,groupId: String, id: String)
    {
        database.child(CHAMPS_CHAT).child(groupId).child("messages").child(id).child(CHAMPS_CHAT_CONTENT).setValue(content).apply {
            addOnFailureListener { BroadcastUtils.sendActionSuccessfull(context, ACTION_CHANGED_MY_MESSAGE_RESULT, false) }
            addOnCompleteListener { BroadcastUtils.sendActionSuccessfull(context, ACTION_CHANGED_MY_MESSAGE_RESULT, true) }
        }
    }

    fun deleteMessage(id: String,groupId: String)
    {
        database.child(CHAMPS_CHAT).child(groupId).child("messages").child(id).removeValue().apply {
            addOnFailureListener { BroadcastUtils.sendActionSuccessfull(context, ACTION_DELETE_MY_MESSAGE_RESULT, false) }
            addOnCompleteListener { BroadcastUtils.sendActionSuccessfull(context, ACTION_DELETE_MY_MESSAGE_RESULT, true) }
        }
    }


    fun getAllMessagesFromGroup(groupId:String)
    {
        messageListener = object : ChildEventListener {

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?)
            {
                BroadcastUtils.sendGetMessageChanged(context, ACTION_CHANGED_MESSAGE_RESULT,
                    dataSnapshot.child(CHAMPS_CHAT_CONTENT).value.toString(), dataSnapshot.key.toString())
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?)
            {
                BroadcastUtils.sendGetMessageResult(context,
                    dataSnapshot.child(CHAMPS_CHAT_SENDER_ID).value.toString(),
                    dataSnapshot.child(CHAMPS_CHAT_DATETIME).value.toString().toLong(),
                    dataSnapshot.child(CHAMPS_CHAT_CONTENT).value.toString(),
                    dataSnapshot.key.toString(),
                    dataSnapshot.child(CHAMPS_CHAT_SENDERURL).value.toString(),
                    dataSnapshot.child(CHAMPS_CHAT_SENDERNAME).value.toString(),
                    dataSnapshot.child(CHAMPS_CHAT_IMAGE).value.toString())
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                BroadcastUtils.sendMessageRemoved(context, ACTION_DELETE_MESSAGE_RESULT,
                    dataSnapshot.key.toString(), dataSnapshot.child(CHAMPS_CHAT_SENDER_ID).value.toString())
            }

            override fun onCancelled(p0: DatabaseError) = BroadcastUtils.sendActionSuccessfull(context, ACTION_GET_MESSAGE_RESULT, false)
        }

        database.child(CHAMPS_CHAT).child(groupId).child("messages").addChildEventListener(messageListener)
    }

    fun removeListener(groupId: String)
    {
        database.child(CHAMPS_CHAT).child(groupId).child("messages").removeEventListener(messageListener)
    }

    fun sendTokenToFB(groupId: String,token:String)
    {
        database.child(CHAMPS_CHAT).child(groupId).child("users").child(App.currentUser!!.id).setValue(token)
    }

    //fonction qui va upload une imageURL à partir de son uri et lui affecter le même uid que le groupe
    fun uploadImage(filePath: Uri?,groupId: String)
    {
        database.child(CHAMPS_CHAT).child(groupId)
        if (filePath != null) {

            val storage: FirebaseStorage = FirebaseStorage.getInstance()
            val storageReference: StorageReference = storage.reference
            val currentTime = System.currentTimeMillis()
            val ref = storageReference.child("$groupId/${App.currentUser?.id}$currentTime")
            ref.putFile(filePath).apply {
                addOnSuccessListener {
                    //BroadcastUtils.sendActionSuccessfull(context, ACTION_UPLOAD_IMAGE_OK, true)
                    val path = "$groupId/${App.currentUser?.id}$currentTime"
                    FirebaseStorage.getInstance().reference.child(path).downloadUrl
                        .addOnSuccessListener { uploadMessage("",groupId,it.toString()) }
                }
                addOnFailureListener { Toast.makeText(context,
                    context.getString(R.string.error_upload_image), Toast.LENGTH_LONG).show() }
            }
        }
    }


}