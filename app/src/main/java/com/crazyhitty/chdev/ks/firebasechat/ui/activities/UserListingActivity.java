package com.crazyhitty.chdev.ks.firebasechat.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.firebasechat.FirebaseChatMainApp;
import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.core.logout.LogoutContract;
import com.crazyhitty.chdev.ks.firebasechat.core.logout.LogoutPresenter;
import com.crazyhitty.chdev.ks.firebasechat.models.FriendlyMessage;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.UserListingPagerAdapter;
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

public class UserListingActivity extends AppCompatActivity implements LogoutContract.View {
    private Toolbar mToolbar;
    private static final int RC_PHOTO_PICKER = 2;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private TabLayout mTabLayoutUserListing;
    private ViewPager mViewPagerUserListing;
    private LogoutPresenter mLogoutPresenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserListingActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int flags) {
        Intent intent = new Intent(context, UserListingActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_listing);
        bindViews();
        init();
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayoutUserListing = (TabLayout) findViewById(R.id.tab_layout_user_listing);
        mViewPagerUserListing = (ViewPager) findViewById(R.id.view_pager_user_listing);
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

    private void init() {
        // set the toolbar
        setSupportActionBar(mToolbar);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseStorage = FirebaseStorage.getInstance();
            storageReference = firebaseStorage.getReference().child("chat_photos");
            // ChatGroupActivity.startActivity(this);
            /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            //only jpj photo
            intent.setType("image*//*");
            // intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            // look here
            startActivityForResult(Intent.createChooser(intent, "Complete Action Using"), RC_PHOTO_PICKER);*/
        }
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
/*
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Constants.ARG_USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(Constants.ARG_USER_status)
                    .setValue("true");*/
            /*DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
            connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    boolean connected = snapshot.getValue(Boolean.class);
                    if (connected) {
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child(Constants.ARG_USERS)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(Constants.ARG_USER_status)
                                .setValue("true");
                    } else {
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child(Constants.ARG_USERS)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(Constants.ARG_USER_status)
                                .setValue("false");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.err.println("Listener was cancelled");
                }
            });
*/        }
        // set the view pager adapter
        UserListingPagerAdapter userListingPagerAdapter = new UserListingPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        mViewPagerUserListing.setAdapter(userListingPagerAdapter);

        // attach tab layout with view pager
        mTabLayoutUserListing.setupWithViewPager(mViewPagerUserListing);

        mLogoutPresenter = new LogoutPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_listing, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mLogoutPresenter.logout();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onLogoutSuccess(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.getMessage();
        }
        LoginActivity.startIntent(this,
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     /*   if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this,"Signed in ",Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this,"Signed in is canceled",Toast.LENGTH_LONG).show();
                finish();
            }
            //should check resultCode
        } else*/
        // if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
        //that before upload
        Uri selectedImageuri = data.getData();
        Bitmap b = null;
        try {
            b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageuri);

        } catch (Exception e) {
            e.getMessage();
        }
        // public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight)
        // {
       /* Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, b.getWidth(), b.getHeight()), new RectF(0, 0, 500, 500), Matrix.ScaleToFit.CENTER);
        Bitmap bm400 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm400.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.getContentUri(bm400);
        selectedImageuri=   Uri.parse(path);*/
        //  }
        // if (bm400)
//call this method like
        //get reference to store file to chat_photos/<filename>
        StorageReference PhotoRef = storageReference.child(selectedImageuri.getLastPathSegment());
          /*  String name = "IMAGE" + new Date().getTime();
            String selectedImage = getImageUri((Bitmap) data.getExtras().get("data"), name);
             image = new Image(new Date().getTime(), name, selectedImage, false);
            ArrayList<Image> localImages = new ArrayList<>();
            localImages.add(image);*/
        //  StorageReference PhotoRef = storageReference.child(name);

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
                        //here why
                        //  getMessagesFromFirebaseGroupUsers(mFriendlyMessage.getSender_uid());
                        // send push notification to the receiver
                     /*sendPushNotificationToReceiver(chat.sender,
                        chat.message,
                        chat.senderUid,
                        new SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN),
                        receiverFirebaseToken);*/
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

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Constants.ARG_USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(Constants.ARG_USER_status)
                    .setValue("true");
        }
    }

    @Override
    protected void onStop() {
        //when exit from home button
        super.onStop();
             if (FirebaseAuth.getInstance().getCurrentUser() != null&& !FirebaseChatMainApp.isChatActivityOpen()) {
                 FirebaseDatabase.getInstance()
                        .getReference()
                        .child(Constants.ARG_USERS)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(Constants.ARG_USER_status)
                        .setValue("false");
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  if(isFinishing()) {
        Log.i("kjf", "jkdjf");
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                FirebaseDatabase.getInstance()
                        .getReference()
                        .child(Constants.ARG_USERS)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(Constants.ARG_USER_status)
                        .setValue("false");
            }
        //}
       /* if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
            connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    boolean connected = snapshot.getValue(Boolean.class);
                    if (connected) {
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child(Constants.ARG_USERS)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(Constants.ARG_USER_status)
                                .setValue("true");
                    } else {
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child(Constants.ARG_USERS)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(Constants.ARG_USER_status)
                                .setValue("false");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.err.println("Listener was cancelled");
                }
            });
        }*/
    }


    @Override
    public void onLogoutFailure(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.getMessage();
        }
    }
}
