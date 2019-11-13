package com.lescigognes.chatentempsreel.common

import java.text.SimpleDateFormat
import java.util.*

/**
 * DATES
 */

val DATETIME_CHAT = SimpleDateFormat("dd/MM HH:mm:ss", Locale.FRANCE)

/***********************************************
 * STORAGE
 **********************************************/
const val STORAGE_ATTACHMENTS = "homework_attachments"

/***********************************************
 * CHAMPS DE LA BASE DE DONNEES
 **********************************************/

//CHAT
const val CHAMPS_CHAT = "chat"
const val CHAMPS_CHAT_SENDER_ID = "senderId"
const val CHAMPS_CHAT_CONTENT = "content"
const val CHAMPS_CHAT_DATETIME = "dateTime"
const val CHAMPS_CHAT_SENDERURL = "senderImage"
const val CHAMPS_CHAT_SENDERNAME = "senderName"
const val CHAMPS_CHAT_IMAGE = "image"

/***********************************************
 * ACTIONS
 **********************************************/
const val PREFIXE_ACTION = "com.lpdream.mandretneffati.studhelp"
const val ACTION_GET_MESSAGE_RESULT = "$PREFIXE_ACTION.getmessageresult"
const val ACTION_SEND_MESSAGE_RESULT = "$PREFIXE_ACTION.sendmessageresult"
const val ACTION_CHANGED_MESSAGE_RESULT = "$PREFIXE_ACTION.changedMessageResult"
const val ACTION_CHANGED_MY_MESSAGE_RESULT = "$PREFIXE_ACTION.changedMyMessageResult"
const val ACTION_DELETE_MESSAGE_RESULT = "$PREFIXE_ACTION.deleteMessageResult"
const val ACTION_DELETE_MY_MESSAGE_RESULT = "$PREFIXE_ACTION.deleteMyMessageResult"
const val ACTION_UPLOAD_IMAGE_OK = "$PREFIXE_ACTION.uploadImageOK"

/***********************************************
 * EXTRA POUR LES INTENT
 **********************************************/


const val EXTRA_SUCCESS = "success"
const val EXTRA_MESSAGE_SENDERID = "messagesenderid"
const val EXTRA_MESSAGE_CONTENT = "messagecontent"
const val EXTRA_MESSAGE_DATETIME = "messagedatetime"
const val EXTRA_MESSAGE_ID = "messageid"
const val EXTRA_MESSAGE_SENDERNAME = "messagesendername"
const val EXTRA_MESSAGE_SENDERULR = "messagesenderurl"
const val EXTRA_MESSAGE_IMAGE = "messageImage"

const val CODE_RETOUR_IMAGE=888
const val CODE_PERMISSIONS_STORAGE = 8888

