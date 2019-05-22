package com.crazyhitty.chdev.ks.firebasechat.core.chat.groupChat;

import android.content.Context;

import com.crazyhitty.chdev.ks.firebasechat.core.chat.ChatContract;
import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.crazyhitty.chdev.ks.firebasechat.models.FriendlyMessage;

import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 9/2/2016 , 10:05 PM
 * Project: FirebaseChat
 */

public class GroupChatPresenter implements GroupChatContract.Presenter, GroupChatContract.OnSendMessageListener,
        GroupChatContract.OnGetChatMessagesListener {
    private GroupChatContract.View mView;
    private GroupChatInteractor mChatInteractor;

    public GroupChatPresenter(GroupChatContract.View view) {
        this.mView = view;
        mChatInteractor = new GroupChatInteractor(this, this);
    }

    @Override
    public void sendMessage(FriendlyMessage chat) {
        mChatInteractor.sendMessageToFirebaseGroupUsers(chat);
    }

    @Override
    public void dismissDiolog() {
        mView.onDismissDialog();
    }

    @Override
    public void getGroupMessages(String senderUid) {
        mChatInteractor.getMessagesFromFirebaseGroupUsers(senderUid);

    }


    @Override
    public void onSendMessageSuccess() {
        mView.onSendMessageSuccess();
    }

    @Override
    public void onSendMessageFailure(String message) {
        mView.onSendMessageFailure(message);
    }


    @Override
    public void onGetChatMessagesSuccess(FriendlyMessage mFriendlyMessage) {
        mView.onGetChatMessagesSuccess(mFriendlyMessage);
    }

    @Override
    public void onGetChatMessagesFailure(String message) {
        {
            mView.onGetChatMessagesFailure(message);
        }

    }
}
