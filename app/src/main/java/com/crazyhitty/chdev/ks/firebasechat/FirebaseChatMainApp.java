package com.crazyhitty.chdev.ks.firebasechat;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Author: Kartik Sharma
 * Created on: 10/16/2016 , 9:35 PM
 * Project: FirebaseChat
 */

public class FirebaseChatMainApp extends Application {
    private static boolean sIsChatActivityOpen = false;
    private static boolean sIsChatFragmentOpen = false;

    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        FirebaseChatMainApp.sIsChatActivityOpen = isChatActivityOpen;
    }
    public static boolean isChatFragmentOpen() {
        return sIsChatFragmentOpen;
    }

    public static void setChatFragmentOpen(boolean isChatActivityOpen) {
        FirebaseChatMainApp.sIsChatFragmentOpen = isChatActivityOpen;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
