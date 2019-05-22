package com.crazyhitty.chdev.ks.firebasechat.core.users.add;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:06 AM
 * Project: FirebaseChat
 */

public interface AddUserContract {
    interface View {
        void onAddUserSuccess(String message);

        void onAddUserFailure(String message);
    }

    interface Presenter {
        void addUser(Context context,String phone, FirebaseUser firebaseUser);
    }

    interface Interactor {
        void addUserToDatabase(Context context,String phone, FirebaseUser firebaseUser);
    }
//for presenter
    interface OnUserDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
