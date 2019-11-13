const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.sendNotifToGroup = functions.database.ref('/chat/1/messages/{pushId}')
  .onCreate((snapshot, context) => {
    var payload =
    {
      data:
      {
        title: "Nouveau message de : " + snapshot.child("senderName").val(),
        body: snapshot.child("content").val(),
        senderId: snapshot.child("senderId").val(),
        senderName : snapshot.child("senderName").val()
      }
    };
    
    return admin.messaging().sendToTopic("messages", payload)
  });



