package com.lescigognes.chatentempsreel.`object`

data class Message(var senderId: String, var content: String, var dateTime: Long, var id: String = "", var senderImage: String = "", var senderName: String,var image:String="")