package com.crazyhitty.chdev.ks.firebasechat.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Author: Kartik Sharma
 * Created on: 9/4/2016 , 12:43 PM
 * Project: FirebaseChat
 */

@IgnoreExtraProperties
public class Chat {
    public String sender;//email
    public String receiver;//email
    public String senderUid;
    public String receiverUid;
    public String message;
    public long timestamp;
    public boolean sent;
    public boolean seen;
    public Chat() {
    }

    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message, long timestamp,boolean sent,boolean seen) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.timestamp = timestamp;
        this.seen=seen;
        this.sent=sent;
    }

    public void setSent(boolean sent) {
        this.sent=sent;
    }

    public boolean getSent() {
        return sent;
    }
    public void setSeen(boolean seen) {
        this.seen=seen;
    }

    public boolean getSeen() {
        return seen;
    }

}
