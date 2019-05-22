package com.crazyhitty.chdev.ks.firebasechat.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.groupChat.GroupChatContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.groupChat.GroupChatPresenter;
import com.crazyhitty.chdev.ks.firebasechat.models.FriendlyMessage;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.GroupChatRecyclerAdapter;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by godaa on 17/09/2017.
 */

public class group_chat extends Fragment implements GroupChatContract.View, SwipeRefreshLayout.OnRefreshListener, TextView.OnEditorActionListener, View.OnClickListener {
    //private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerViewAllUserListing;
    private EditText Message_toSend;
    private Button Send_button;
    private ImageButton Photo_select_toSend;
    GroupChatPresenter mGroupChatPresenter;
    private static final int RC_PHOTO_PICKER = 2;
    GroupChatRecyclerAdapter mGroupChatRecyclerAdapter;
    Context context;

    public static group_chat newInstance(Context context) {
        Bundle args = new Bundle();

        group_chat fragment = new group_chat();
        fragment.setArguments(args);
        // ChatGroupActivity.startActivity(context);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_group_users, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        // mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerViewAllUserListing = (RecyclerView) view.findViewById(R.id.recycler_view_all_user_listing);
        Message_toSend = (EditText) view.findViewById(R.id.messageEditText);
        Send_button = (Button) view.findViewById(R.id.sendButton);
        Photo_select_toSend = (ImageButton) view.findViewById(R.id.photoPickerButton);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {

       /* mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });*/
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(),"Please Connect Wifi And try again",Toast.LENGTH_SHORT).show();

            return;
        }
        mProgressDialog = new ProgressDialog(getActivity());

        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        Photo_select_toSend.setOnClickListener(this);
        // mSwipeRefreshLayout.setOnRefreshListener(this);
        Send_button.setOnClickListener(this);
        Message_toSend.setOnEditorActionListener(this);
       // if (mGroupChatRecyclerAdapter == null) {
            mGroupChatRecyclerAdapter = new GroupChatRecyclerAdapter(new ArrayList<FriendlyMessage>());
            mRecyclerViewAllUserListing.setAdapter(mGroupChatRecyclerAdapter);
       // }
// Enable Send button when there's text to send
        Message_toSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    Send_button.setEnabled(true);
                } else {
                    Send_button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
        mGroupChatPresenter = new GroupChatPresenter(this);
        getChats();
    }
     boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    public void getChats() {
        mGroupChatPresenter.getGroupMessages(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    //here to set send action
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            sendMessage();
            return true;
        }
        return false;
    }


    @Override
    public void onRefresh() {
        //getChats();
    }


    @Override
    public void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child(Constants.ARG_USERS)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(Constants.ARG_USER_status)
                        .setValue("true");
            }

        //  EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //    EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void sendMessage() {
        String message = Message_toSend.getText().toString();
      /*  String receiver = getArguments().getString(Constants.ARG_RECEIVER);
        String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
        */
        if (message.equals("")) {
            return;
        }
        String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FriendlyMessage friendlyMessage = new FriendlyMessage(message, "", sender, senderUid, System.currentTimeMillis());
        send(friendlyMessage);
       /* Chat chat = new Chat(sender,
                receiver,
                senderUid,
                receiverUid,
                message,
                System.currentTimeMillis());*/
    }

    public void send(FriendlyMessage friendlyMessage) {
        mGroupChatPresenter.sendMessage(friendlyMessage);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSendMessageSuccess() {
        Message_toSend.setText("");
        try {
            Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();

        } catch (Exception i) {
            i.getMessage();
        }
    }

    @Override
    public void onDismissDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void onSendMessageFailure(String message) {
        try {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

        } catch (Exception i) {
            i.getMessage();
        }
    }


    @Override
    public void onGetChatMessagesSuccess(FriendlyMessage mFriendlyMessage) {
       /* mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mUserListingRecyclerAdapter = new UserListingRecyclerAdapter(users);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();*/
       /* mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });*/
        mProgressDialog.dismiss();
        if (mGroupChatRecyclerAdapter == null) {
            mGroupChatRecyclerAdapter = new GroupChatRecyclerAdapter(new ArrayList<FriendlyMessage>());
            mRecyclerViewAllUserListing.setAdapter(mGroupChatRecyclerAdapter);
        }
        mGroupChatRecyclerAdapter.add(mFriendlyMessage);
        mRecyclerViewAllUserListing.smoothScrollToPosition(mGroupChatRecyclerAdapter.getItemCount() - 1);
      /*  mGroupChatRecyclerAdapter = new UserListingRecyclerAdapter(users);
        mRecyclerViewAllUserListing.setAdapter(mGroupChatRecyclerAdapter);
        mGroupChatRecyclerAdapter.notifyDataSetChanged();*/
    }


    @Override
    public void onGetChatMessagesFailure(String message) {
       /* mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });*/

        try {
            mProgressDialog.dismiss();

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

        } catch (Exception i) {
            i.getMessage();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photoPickerButton:
                // ChatGroupActivity.startActivity(getContext());
                //  UserListingActivity.startActivity(getContext());
                mProgressDialog.show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //only jpj photo
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                // look here
                startActivityForResult(Intent.createChooser(intent, "Complete Action Using"), RC_PHOTO_PICKER);
                // getChats();
           /*
                try {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                     getActivity().startActivityForResult(takePicture, RC_PHOTO_PICKER);
                } catch (SecurityException s) {
                    s.getMessage();
                }*/
                break;
            case R.id.sendButton:
                //text or photo url is null and here photurl is null
                //here update database for firbase
                // FriendlyMessage friendlyMessage = new FriendlyMessage(Message_toSend.getText().toString(), null, FirebaseAuth.getInstance().getCurrentUser().getEmail());
                // mDatabaseReference.push().setValue(friendlyMessage);
                sendMessage();

                // Clear input box
                Message_toSend.setText("");
                break;
            default:
                break;
        }
    }
}
