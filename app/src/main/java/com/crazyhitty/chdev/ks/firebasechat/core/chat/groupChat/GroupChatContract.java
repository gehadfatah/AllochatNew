package com.crazyhitty.chdev.ks.firebasechat.core.chat.groupChat;

import android.content.Context;

import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.crazyhitty.chdev.ks.firebasechat.models.FriendlyMessage;
import com.crazyhitty.chdev.ks.firebasechat.models.User;

import java.util.List;

/**
 * Created by godaa on 18/09/2017.
 */

public interface GroupChatContract {
    interface View {
        void onSendMessageSuccess();

        void onDismissDialog();
        void onSendMessageFailure(String message);

        void onGetChatMessagesSuccess(FriendlyMessage mFriendlyMessages);

        void onGetChatMessagesFailure(String message);
    }

    interface Presenter {
        void sendMessage( FriendlyMessage friendlyMessage);

        void dismissDiolog();
        void getGroupMessages(String senderUid);
    }

    interface Interactor {
        void sendMessageToFirebaseGroupUsers(  FriendlyMessage friendlyMessage);

        void getMessagesFromFirebaseGroupUsers(String senderUid);
    }

    interface OnSendMessageListener {
        void onSendMessageSuccess();

        void onSendMessageFailure(String message);
    }

    interface OnGetChatMessagesListener {
        void onGetChatMessagesSuccess(FriendlyMessage mFriendlyMessage);

        void onGetChatMessagesFailure(String message);
    }
}


