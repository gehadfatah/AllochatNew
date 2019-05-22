package com.crazyhitty.chdev.ks.firebasechat.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.firebasechat.FirebaseChatMainApp;
import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.ChatContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.ChatPresenter;
import com.crazyhitty.chdev.ks.firebasechat.events.PushNotificationEvent;
import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.ChatRecyclerAdapter;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.crazyhitty.chdev.ks.firebasechat.utils.SharedPrefUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 10:36 AM
 * Project: FirebaseChat
 */

public class ChatFragment extends Fragment implements ChatContract.View, TextView.OnEditorActionListener,Observer {
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    private ImageButton sendButton;
    private ProgressDialog mProgressDialog;
   // observ observ=new observ();

    String[] chatNseen;
    int ichtnseen;
    private  ChatRecyclerAdapter mChatRecyclerAdapter;
Chat chat_last;
    private ChatPresenter mChatPresenter;

    public static ChatFragment newInstance(String receiver,
                                           String receiverUid,
                                           String firebaseToken) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_RECEIVER, receiver);
        args.putString(Constants.ARG_RECEIVER_UID, receiverUid);
        args.putString(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

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
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void bindViews(View view) {
        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) view.findViewById(R.id.edit_text_message);
        sendButton = (ImageButton) view.findViewById(R.id.send_button);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mETxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mETxtMessage.setOnEditorActionListener(this);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        mChatPresenter = new ChatPresenter(this);
        //for message come from other for first chat andr set listener to that
        mChatPresenter.getMessage(getContext(), FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getArguments().getString(Constants.ARG_RECEIVER_UID));
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
    public void onResume() {
        super.onResume();
        ChatFragment.getObservable().addObserver(this);
        FirebaseChatMainApp.setChatFragmentOpen(true);
        new SharedPrefUtil(getContext()).saveString(Constants.ARG_FIREBASE_TOKEN_for_recevier, getArguments().getString(Constants.ARG_FIREBASE_TOKEN));
    }

    private void sendMessage() {
        String message = mETxtMessage.getText().toString();
        if (message.equals("")) {
            return;
        }
        String receiver = getArguments().getString(Constants.ARG_RECEIVER);
        String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
        String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
        Chat chat = new Chat(sender,
                receiver,
                senderUid,
                receiverUid,
                message,
                System.currentTimeMillis(),
                false, false);
        mChatPresenter.sendMessage(getActivity().getApplicationContext(),
                chat,
                receiverFirebaseToken);
    }

    @Override
    public void onSendMessageSuccess(Chat chat) {
        mETxtMessage.setText("");
        // FirebaseDatabase.getInstance().getReference().child()
        try {
            Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();

        } catch (Exception i) {
            i.getMessage();
        }
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
    public void onGetMessagesSuccess(Chat chat) {
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecyclerAdapter(new ArrayList<Chat>());
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
        mChatRecyclerAdapter.add(chat);
      //  chat_last=chat;
       // if (!chat.getSeen()) {
            //  chatNseen = new String[]{"0"};
         //   ichtnseen++;
            // new SharedPrefUtil(getContext()).savearrayString("chatsNseen", chatNseen);
       // }
        //  mChatRecyclerAdapter.setseen();
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
    }

    @Override
    public void onPause() {
        super.onPause();
        FirebaseChatMainApp.setChatFragmentOpen(false);

    }

    @Override
    public void onDestroy() {
        ChatFragment.getObservable().deleteObserver(this);

        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onGetMessagesFailure(String message) {
        try {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

        } catch (Exception i) {
            i.getMessage();
        }
    }

    public void cah() {
      /*  observ.getObservable().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
             mChatRecyclerAdapter.setseen(1);
            }
        });*/
    }
    //here for use eventBus
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)

    @Subscribe(threadMode = ThreadMode.MAIN)
    //@Subscribe
    public void onPushNotificationEvent(PushNotificationEvent pushNotificationEvent) {
        //for first chat for two sender and recevier
        //just if you open chat fragment for register and unregister event bus
        // for (int i=0;i<ichtnseen;i++) {

       // mChatRecyclerAdapter.setseen(ichtnseen);
        //mChatRecyclerAdapter.notifyDataSetChanged();
       // onGetMessagesSuccess(chat_last);
       // mChatRecyclerAdapter.
        // ichtnseen = 0;
        //  }
        if (mChatRecyclerAdapter == null || mChatRecyclerAdapter.getItemCount() == 0) {
            mChatPresenter.getMessage(getContext(), FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    pushNotificationEvent.getUid());
        }else if (pushNotificationEvent.getSendOrSeen().equals("seen")){
            Log.i("", "ls");
            ChatFragment.observ.newInstanse().change_state();
           // mChatRecyclerAdapter.setseen(1);

            // observ.getObservable().addObserver(this);
        }
    }

   /* public  static void uda() {
        ChatFragment.observ.newInstanse().change_state();

    }*/
    @Override
    public void update(Observable o, Object arg) {
      //  mChatRecyclerAdapter.setseen(1);
        int i=mChatRecyclerAdapter.getItemCount();
        for (int r=0;r<i;r++) {
            Chat chatseen = mChatRecyclerAdapter.mChats.get(r);
            chatseen.setSeen(true);
            mChatRecyclerAdapter.notifyItemChanged(r);
          //  ArrayList<float > chats=new ArrayList<>();
        }
    }

    public static class observ extends Observable {
        private static   observ instance = null;

        private observ() {
           // getNewDataFromRemote();
        }


        public static observ newInstanse() {
            if (instance == null) {

                instance = new observ();
            }
            return instance;
        }

        public void change_state() {
            setChanged();
            notifyObservers();
        }

        // Simulate network
      /*  private void getNewDataFromRemote() {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    change_state();
                }
            }, 0);
        }*/


    }
    public static observ getObservable() {
        return observ.newInstanse();
    }

}
