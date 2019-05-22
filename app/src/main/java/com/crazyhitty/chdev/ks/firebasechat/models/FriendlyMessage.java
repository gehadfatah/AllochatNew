package com.crazyhitty.chdev.ks.firebasechat.models;

public class FriendlyMessage {
//same as rules in firbase Database
    private String text;
    /*private String name;*/
    private String photoUrl;
    private String Email_sender;
    private String sender_uid;
    public long timestamp;

    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, /*String name,*/ String photoUrl,String Email_sender,String senderUid,long timestamp) {
        this.text = text;
       /* this.name = name;*/
        this.photoUrl = photoUrl;
        this.sender_uid=senderUid;
        this.Email_sender=Email_sender;
        this.timestamp=timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

 /*   public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }*/

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public String getEmail_sender() {
        return Email_sender;
    }

    public void setEmail_sender(String EmailSender) {
        this.Email_sender = EmailSender;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }
}
