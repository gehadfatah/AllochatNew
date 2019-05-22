package com.crazyhitty.chdev.ks.firebasechat.core.users.get.all;

import android.text.TextUtils;

import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 9/2/2016 , 10:08 PM
 * Project: FirebaseChat
 */
public class GetUsersInteractor implements GetUsersContract.Interactor {
    private static final String TAG = "GetUsersInteractor";
    private GetUsersContract.OnGetAllUsersListener mOnGetAllUsersListener;

    public GetUsersInteractor(GetUsersContract.OnGetAllUsersListener onGetAllUsersListener) {
        this.mOnGetAllUsersListener = onGetAllUsersListener;
    }
    @Override
    public void getAllUsersFromFirebase(final boolean loginOrchat) {
        //add listener for one time and add it to Arg_users child
        //here
        //see if get newest user is register
        //This pattern can be useful when you want to fetch all children of a list in a single
        // operation, rather than listening for additional onChildAdded events.
        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                /*for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                }*/
                List<User> users = new ArrayList<>();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    //for return object from FCM server
                    try {
                        User user = dataSnapshotChild.getValue(User.class);
                        if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())||loginOrchat ){
                            users.add(user);
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }

                mOnGetAllUsersListener.onGetAllUsersSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
             //   mOnGetAllUsersListener.onGetAllUsersFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getChatUsersFromFirebase() {
        /*FirebaseDatabase.getInstance().getReference().child(Constants.ARG_CHAT_ROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots=dataSnapshot.getChildren().iterator();
                List<User> users=new ArrayList<>();
                while (dataSnapshots.hasNext()){
                    DataSnapshot dataSnapshotChild=dataSnapshots.next();
                    dataSnapshotChild.getRef().
                    Chat chat=dataSnapshotChild.getValue(Chat.class);
                    if(chat.)4
                    if(!TextUtils.equals(user.uid,FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

           }
        });*/
    }
}
