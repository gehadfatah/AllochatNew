package com.crazyhitty.chdev.ks.firebasechat.core.chat.groupChat;

import android.content.Context;
import android.util.Log;

import com.crazyhitty.chdev.ks.firebasechat.core.chat.ChatContract;
import com.crazyhitty.chdev.ks.firebasechat.fcm.FcmNotificationBuilder;
import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.crazyhitty.chdev.ks.firebasechat.models.FriendlyMessage;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.crazyhitty.chdev.ks.firebasechat.utils.SharedPrefUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Author: Kartik Sharma
 * Created on: 9/2/2016 , 10:08 PM
 * Project: FirebaseChat
 */

public class GroupChatInteractor implements GroupChatContract.Interactor {
    private static final String TAG = "ChatGroupInteractor";

    private GroupChatContract.OnSendMessageListener mOnSendMessageListener;
    private GroupChatContract.OnGetChatMessagesListener mOnGetMessagesListener;

    public GroupChatInteractor(GroupChatContract.OnSendMessageListener onSendMessageListener) {
        this.mOnSendMessageListener = onSendMessageListener;
    }

    public GroupChatInteractor(GroupChatContract.OnGetChatMessagesListener onGetMessagesListener) {
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    public GroupChatInteractor(GroupChatContract.OnSendMessageListener onSendMessageListener,
                               GroupChatContract.OnGetChatMessagesListener onGetMessagesListener) {
        this.mOnSendMessageListener = onSendMessageListener;
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    @Override
    public void sendMessageToFirebaseGroupUsers(/*final Context context,*/ final FriendlyMessage mFriendlyMessage/*, final String receiverFirebaseToken*/) {
//        final String room_type_1 = chat.senderUid + "_" + chat.receiverUid;
//        final String room_type_2 = chat.receiverUid + "_" + chat.senderUid;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        /*Use vel (ValueEventListener) to populate the recyclerview with the list of current data at dbref. Something like this: vel = dbref.addValueEventListener(vel);
        Once i have the list of current data at dbref, I remove the listener vel from dbref : dbref.removeEventListener(vel);. This is not necessary if you used dbref.addListenerForSingleValueEvent(vel); in step 1.
        Write a Query query to filter new posts inserted at dbref from now on. Something like this: query = dbref.orderByChild(post.createdat).startAt(System.currentTimeMillis())
        Attach a cel (ChildEventListener) to query to receive only new posts : query.addChildEventListener(cel);
        databaseReference.child(Constants.ARG_CHAT_ROOMS).getRef().orderByChild().startAt()
        */
        //Attaching a ValueEventListener to a list of data will return the entire
        // list of data as a single DataSnapshot, which you can then loop over to access individual children.
        databaseReference.child(Constants.ARG_CHAT_group_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    //for first chat from sender to receiver
                    Log.e(TAG, "sendMessageToFirebaseUser: success");
                //                if (dataSnapshot.hasChild(room_type_1)) {
                if (dataSnapshot.hasChildren()) {

                    databaseReference.child(Constants.ARG_CHAT_group_ROOMS)./*child(room_type_1).*/child(String.valueOf(mFriendlyMessage.timestamp)).setValue(mFriendlyMessage);
                }else {
                    databaseReference.child(Constants.ARG_CHAT_group_ROOMS)./*child(room_type_1).*/child(String.valueOf(mFriendlyMessage.timestamp)).setValue(mFriendlyMessage);
                    //for first chat
                    getMessagesFromFirebaseGroupUsers(mFriendlyMessage.getSender_uid());

                }
                   //here why
                // send push notification to the receiver
                /*sendPushNotificationToReceiver(chat.sender,
                        chat.message,
                        chat.senderUid,
                        new SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN),
                        receiverFirebaseToken);*/
                mOnSendMessageListener.onSendMessageSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }

   /* private void sendPushNotificationToReceiver(String username,
                                                String message,
                                                String uid,
                                                String firebaseToken,
                                                String receiverFirebaseToken) {
        FcmNotificationBuilder.initialize()
                .title(username)
                .message(message)
                .username(username)
                .uid(uid)
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(receiverFirebaseToken)
                .send();
    }*/

    @Override
    public void getMessagesFromFirebaseGroupUsers(String senderUid) {
       // final String room_type_1 = senderUid + "_" + receiverUid;
       // final String room_type_2 = receiverUid + "_" + senderUid;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        // In some cases you may want a callback to be called once and
        // then immediately removed, such as when initializing a UI element that you don't
        // expect to change. You can use the addListenerForSingleValueEvent() method to simplify
        // this scenario: it triggers once and then does not trigger again.
        // This is useful for data that only needs to be loaded once and isn't expected to change
        // frequently or require active listening. For instance, the blogging app in the previous
        // examples uses this method to load a user's profile when they begin authoring a new post:
        databaseReference.child(Constants.ARG_CHAT_group_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
           //Read and listen for changes to the entire contents of a path
           // This method is triggered once when the listener
           // is attached and again every time the data, including children, changes
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               /* if (dataSnapshot.hasChildren()) {*/
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                if (!dataSnapshots.hasNext()) {
                    mOnGetMessagesListener.onGetChatMessagesFailure("No Message Yet");
                    return;
                }

                try {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constants.ARG_CHAT_group_ROOMS)
                            .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            FriendlyMessage mFriendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                            mOnGetMessagesListener.onGetChatMessagesSuccess(mFriendlyMessage);

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //for if you get logout where you in chat group
                           // mOnGetMessagesListener.onGetChatMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }

                    });
            } catch (Exception e) {
                e.getMessage();
            }
                }

           // }

            @Override
            public void onCancelled(DatabaseError databaseError) {
          //      mOnGetMessagesListener.onGetChatMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });

    }

}
