package com.crazyhitty.chdev.ks.firebasechat.core.login;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.crazyhitty.chdev.ks.firebasechat.utils.SharedPrefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:10 AM
 * Project: FirebaseChat
 */

public class LoginInteractor implements LoginContract.Interactor {
    private LoginContract.OnLoginListener mOnLoginListener;

    public LoginInteractor(LoginContract.OnLoginListener onLoginListener) {
        this.mOnLoginListener = onLoginListener;
    }

    @Override
    public void performFirebaseLogin(final Activity activity, String email, String password, boolean PhoneOrNot) {
        /*FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS)
                .child(uid)*/
       /* if (PhoneOrNot) {
            email = new SharedPrefUtil(activity).getString(Constants.ARG_USER_default_EMAIL, "");
        }*/
        if (!email.equals("") && !password.equals("")) {
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "performFirebaseLogin:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (task.isSuccessful()) {
                                mOnLoginListener.onSuccess(task.getResult().toString());
                                //here
                                //if firebase token is change for every login
                                //or just check to update firebase token
                                updateUserStatus(task.getResult().getUser().getUid());
                                updateFirebaseToken(task.getResult().getUser().getUid(),
                                        new SharedPrefUtil(activity.getApplicationContext()).getString(Constants.ARG_FIREBASE_TOKEN, null));
                            } else {
                                mOnLoginListener.onFailure(task.getException().getMessage());
                            }
                        }
                    });
        } else {
            Toast.makeText(activity.getApplicationContext(), "please text correct email or phone", Toast.LENGTH_LONG).show();
        }
    }

    private void updateFirebaseToken(String uid, String token) {
        // if (FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).child(uid).equals())
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_FIREBASE_TOKEN)
                .setValue(token);
    }

    private void updateUserStatus(String uid) {

        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_USER_status)
                .setValue("true");
    }
}
