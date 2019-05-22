package com.crazyhitty.chdev.ks.firebasechat.core.logout;

import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:10 AM
 * Project: FirebaseChat
 */

public class LogoutInteractor implements LogoutContract.Interactor {
    private LogoutContract.OnLogoutListener mOnLogoutListener;

    public LogoutInteractor(LogoutContract.OnLogoutListener onLogoutListener) {
        mOnLogoutListener = onLogoutListener;
    }

    @Override
    public void performFirebaseLogout() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            updateUserStatus(FirebaseAuth.getInstance().getCurrentUser().getUid());

            FirebaseAuth.getInstance().signOut();
            mOnLogoutListener.onSuccess("Successfully logged out!");
        } else {
            mOnLogoutListener.onFailure("No user logged in yet!");
        }
    }
    private void updateUserStatus(String uid) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_USER_status)
                .setValue("false");
    }
}
