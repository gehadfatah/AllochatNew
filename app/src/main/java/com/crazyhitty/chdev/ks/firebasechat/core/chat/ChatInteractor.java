package com.crazyhitty.chdev.ks.firebasechat.core.chat;

import android.content.Context;
import android.util.Log;

import com.crazyhitty.chdev.ks.firebasechat.FirebaseChatMainApp;
import com.crazyhitty.chdev.ks.firebasechat.fcm.FcmNotificationBuilder;
import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.crazyhitty.chdev.ks.firebasechat.utils.SharedPrefUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Author: Kartik Sharma
 * Created on: 9/2/2016 , 10:08 PM
 * Project: FirebaseChat
 */

public class ChatInteractor implements ChatContract.Interactor {
    private static final String TAG = "ChatInteractor";

    private ChatContract.OnSendMessageListener mOnSendMessageListener;
    private ChatContract.OnGetMessagesListener mOnGetMessagesListener;

    public ChatInteractor(ChatContract.OnSendMessageListener onSendMessageListener) {
        this.mOnSendMessageListener = onSendMessageListener;
    }

    public ChatInteractor(ChatContract.OnGetMessagesListener onGetMessagesListener) {
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    public ChatInteractor(ChatContract.OnSendMessageListener onSendMessageListener,
                          ChatContract.OnGetMessagesListener onGetMessagesListener) {
        this.mOnSendMessageListener = onSendMessageListener;
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    @Override
    public void sendMessageToFirebaseUser(final Context context, final Chat chat, final String receiverFirebaseToken) {
        final String room_type_1 = chat.senderUid + "_" + chat.receiverUid;
        final String room_type_2 = chat.receiverUid + "_" + chat.senderUid;
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        /*Use vel (ValueEventListener) to populate the recyclerview with the list of current data at dbref. Something like this: vel = dbref.addValueEventListener(vel);
        Once i have the list of current data at dbref, I remove the listener vel from dbref : dbref.removeEventListener(vel);. This is not necessary if you used dbref.addListenerForSingleValueEvent(vel); in step 1.
        Write a Query query to filter new posts inserted at dbref from now on. Something like this: query = dbref.orderByChild(post.createdat).startAt(System.currentTimeMillis())
        Attach a cel (ChildEventListener) to query to receive only new posts : query.addChildEventListener(cel);
        databaseReference.child(Constants.ARG_CHAT_ROOMS).getRef().orderByChild().startAt()
        */
        //Attaching a ValueEventListener to a list of data will return the entire
        // list of data as a single DataSnapshot, which you can then loop over to access individual children.
        databaseReference.child(Constants.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //the message is saved to one  time and just one
                //set one room for two partner
                chat.setSent(true);
                if (dataSnapshot.hasChild(room_type_1)) {
                    //for if there is room from ahmed to mohamoud
                    Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                    databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    //for if there is room from mahamoud to ahmed
                    Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                    databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_type_2).child(String.valueOf(chat.timestamp)).setValue(chat);

                } else {
                    //for first chat for sender and receiver
                    Log.e(TAG, "sendMessageToFirebaseUser: success");
                    databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
                   //here why
                    //because for first chat to set listener not to second and so
                    //because you set listener for first chat
                    getMessageFromFirebaseUser(context,chat.senderUid, chat.receiverUid);
                }
                // send push notification to the receiver
                sendPushNotificationToReceiver(chat.sender,
                        chat.message,
                        chat.senderUid,
                        new SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN),
                        receiverFirebaseToken,"send");
                mOnSendMessageListener.onSendMessageSuccess(chat);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }

    private void sendPushNotificationToReceiver(String username,
                                                String message,
                                                String uid,
                                                String firebaseToken,
                                                String receiverFirebaseToken,String sendOrSeen) {
        FcmNotificationBuilder.initialize()
                .title(username)
                .message(message)
                .username(username)
                .uid(uid)
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(receiverFirebaseToken)
                .sendOrSeen(sendOrSeen)
                .send();
    }
//set listener for two thing message come from other and message send by me
    @Override
    public void getMessageFromFirebaseUser(final Context context, final String senderUid, String receiverUid) {
        final String room_type_1 = senderUid + "_" + receiverUid;
        final String room_type_2 = receiverUid + "_" + senderUid;
      // final String room_for_seen;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        // In some cases you may want a callback to be called once and
        // then immediately removed, such as when initializing a UI element that you don't
        // expect to change. You can use the addListenerForSingleValueEvent() method to simplify
        // this scenario: it triggers once and then does not trigger again.
        // This is useful for data that only needs to be loaded once and isn't expected to change
        // frequently or require active listening. For instance, the blogging app in the previous
        // examples uses this method to load a user's profile when they begin authoring a new post:
        databaseReference.child(Constants.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
           //Read and listen for changes to the entire contents of a path
           // This method is triggered once when the listener
           // is attached and again every time the data, including children, changes
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for set two listner
                //see here he see if there is rooms first before listen to anything
                //if user open fragment and not do anything and exit
                //and set listner for just one  of room_type not for two
                if (dataSnapshot.hasChild(room_type_1)) {
                   // room_for_seen=room_type_1;
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_1 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constants.ARG_CHAT_ROOMS)
                            .child(room_type_1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            //chat.setSeen(true);
                            if (chat.receiverUid.equals(senderUid)&& FirebaseChatMainApp.isChatFragmentOpen()&&!chat.getSeen()) {
                                SetSeen(chat, room_type_1);
                                // chat.setSeen(true);
                                // send push notification to the sender that has sent me message and i seen it
                                sendPushNotificationToReceiver(chat.receiver,
                                        chat.message,
                                        chat.receiverUid,
                                        new SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN),
                                        new SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN_for_recevier),"seen");
                            }

                                mOnGetMessagesListener.onGetMessagesSuccess(chat);


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
                           // mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                }
                //for listen to chat come from other
                else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_2 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constants.ARG_CHAT_ROOMS)
                            .child(room_type_2).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            //for old and new chat
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            if (chat.receiverUid.equals(senderUid)&& !chat.getSeen()&&FirebaseChatMainApp.isChatFragmentOpen()) {
                                SetSeen(chat,room_type_2);
                             //   chat.setSeen(true);
                                // send push notification to the receiver
                                sendPushNotificationToReceiver(chat.receiver,
                                        chat.message,
                                        chat.receiverUid,
                                        new SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN),
                                        new SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN_for_recevier),"seen");
                            }
                            mOnGetMessagesListener.onGetMessagesSuccess(chat);
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
                           // mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                } else {
                    Log.e(TAG, "getMessageFromFirebaseUser: no such room available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            //    mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });
    }

    public void SetSeen(Chat chat,String room) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Constants.ARG_CHAT_ROOMS)
                    .child(room).
                    child(String.valueOf(chat.timestamp))
                    .child(Constants.ARG_CHAT_SEEN)
                    .setValue(true);
        }
    }
}
