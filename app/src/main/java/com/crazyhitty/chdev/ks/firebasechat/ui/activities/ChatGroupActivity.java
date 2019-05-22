package com.crazyhitty.chdev.ks.firebasechat.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.crazyhitty.chdev.ks.firebasechat.FirebaseChatMainApp;
import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.groupChat.send_photo;
import com.crazyhitty.chdev.ks.firebasechat.models.FriendlyMessage;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ChatGroupActivity extends AppCompatActivity implements send_photo {
    private Toolbar mToolbar;
    private static final int RC_PHOTO_PICKER = 2;
    private DatabaseReference mDatabaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Context context = this;

    public static void startActivity(Context context
                                    /* String receiver,
                                     String receiverUid,
                                     String firebaseToken*/) {
        Intent intent = new Intent(context, ChatGroupActivity.class);
      /*  intent.putExtra(Constants.ARG_RECEIVER, receiver);
        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constants.ARG_FIREBASE_TOKEN, firebaseT
        oken);*/
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bindViews();
        init();
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void init() {
        // set the toolbar
        setSupportActionBar(mToolbar);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseStorage = FirebaseStorage.getInstance();
            storageReference = firebaseStorage.getReference().child("chat_photos");
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            //only jpj photo
            intent.setType("image/*");
            // intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            // look here
            startActivityForResult(Intent.createChooser(intent, "Complete Action Using"), RC_PHOTO_PICKER);

        }
        // set toolbar title
        //   mToolbar.setTitle("group");

        // set the register screen fragment
        /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_chat,
                group_chat.newInstance(this),
                group_chat.class.getSimpleName());
        fragmentTransaction.commit();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseChatMainApp.setChatActivityOpen(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseChatMainApp.setChatActivityOpen(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //UserListingActivity.startActivity(context);
        //  if(true)return;
     /*   if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this,"Signed in ",Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this,"Signed in is canceled",Toast.LENGTH_LONG).show();
                finish();
            }
            //should check resultCode
        } else*/
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            //that before upload
            Uri selectedImageuri = data.getData();
            //get reference to store file to chat_photos/<filename>
            StorageReference PhotoRef = storageReference.child(selectedImageuri.getLastPathSegment());
            //upload file to firbase storage
            /*PhotoRef.putFile(selectedImageuri).addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    task.getResult().getDownloadUrl()
                }
            });*/
            PhotoRef.putFile(selectedImageuri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri dowloadUri = taskSnapshot.getDownloadUrl();
                    //text or photo url is null and here text is null
                    //here i save photourl of database in firebase
                    String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    final FriendlyMessage friendlyMessage = new FriendlyMessage("", dowloadUri.toString(), sender, senderUid, System.currentTimeMillis());
                    // Use the push() method to append data to a list in multiuser applications. The push() method
                    //generates a unique key every time a new child is added to the specified Firebase reference
                    // mDatabaseReference.push().setValue(friendlyMessage);
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
                            // Log.e(TAG, "sendMessageToFirebaseUser: success");
                            databaseReference.child(Constants.ARG_CHAT_group_ROOMS)./*child(room_type_1).*/child(String.valueOf(friendlyMessage.timestamp)).setValue(friendlyMessage);
                            //   if(true)return;
                            //here why
                            /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout_content_chat,
                                    group_chat.newInstance(getApplicationContext()),
                                    group_chat.class.getSimpleName());
                            fragmentTransaction.commit(); */
                            //  mOnSendMessageListener.onSendMessageSuccess();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //   mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
                        }
                    });

                }
            });
        }
    }

    @Override
    public void send() {

    }
}
