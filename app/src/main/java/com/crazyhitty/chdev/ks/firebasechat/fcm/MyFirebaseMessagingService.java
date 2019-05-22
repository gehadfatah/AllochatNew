package com.crazyhitty.chdev.ks.firebasechat.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.crazyhitty.chdev.ks.firebasechat.FirebaseChatMainApp;
import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.events.PushNotificationEvent;
import com.crazyhitty.chdev.ks.firebasechat.ui.activities.ChatActivity;
import com.crazyhitty.chdev.ks.firebasechat.ui.fragments.ChatFragment;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
//here you can send notification if app in background or not
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String title = remoteMessage.getData().get("title");
            String SendOrSeen = remoteMessage.getData().get("sendOrSeen");
            String message = remoteMessage.getData().get("text");
            String username = remoteMessage.getData().get("username");
            //for sender Uid
            String uid = remoteMessage.getData().get("uid");
            String fcmToken = remoteMessage.getData().get("fcm_token");

            // Don't show notification if chat activity is open.
            if (!FirebaseChatMainApp.isChatActivityOpen()) {
                /*sendNotification(title,
                        message,
                        username,
                        uid,
                        fcmToken);*/
           // } else if (SendOrSeen.equals("seen")) {
                  //  ChatFragment.uda();
            }else {
                //get here for every chat or notification send to me and i open chatapp
                //and the subscrubi will recevie it if he register eventBus
                    EventBus.getDefault().post(new PushNotificationEvent(title,
                            message,
                            username,
                            uid,
                            fcmToken, SendOrSeen));
              /*  if (SendOrSeen.equals("seen")){
                    Log.i("", "ls");
                    ChatFragment.observ.newInstanse().change_state();
                    // mChatRecyclerAdapter.setseen(1);

                    // observ.getObservable().addObserver(this);
                }*/

            }
        }
    }
    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(String title,
                                  String message,
                                  String receiver,
                                  String receiverUid,
                                  String firebaseToken) {
        Intent intent = new Intent(this, ChatActivity.class);
        //for username or email
        intent.putExtra(Constants.ARG_RECEIVER, receiver);
        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.download)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}