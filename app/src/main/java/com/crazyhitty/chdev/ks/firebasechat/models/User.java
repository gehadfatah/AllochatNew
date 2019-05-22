package com.crazyhitty.chdev.ks.firebasechat.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Author: Kartik Sharma
 * Created on: 9/1/2016 , 8:35 PM
 * Project: FirebaseChat
 */

@IgnoreExtraProperties
public class User {
    public String uid;
    public String email;
    public String firebaseToken;
    public String phone;
    public String status;
    public String password;

    public User() {
    }

    public User(String uid, String email, String firebaseToken, String phone, String status,String password) {
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
        this.phone = phone;
        this.status = status;
        this.password=password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone=phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password=password;
    }
 /*   public void sets(boolean online) {
        this.online = online;

    }

    public boolean getonline() {
        return this.online;
    }*/
}
