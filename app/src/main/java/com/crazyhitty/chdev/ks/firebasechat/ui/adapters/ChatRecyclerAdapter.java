package com.crazyhitty.chdev.ks.firebasechat.ui.adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.google.firebase.auth.FirebaseAuth;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;

/**
 * Author: Kartik Sharma
 * Created on: 10/16/2016 , 10:36 AM
 * Project: FirebaseChat
 */

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    public List<Chat> mChats;

    public ChatRecyclerAdapter(List<Chat> chats) {
        mChats = chats;
    }

    public List<Chat> setchats() {
        return mChats;
    }
    public void add(Chat chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    public void setseen(int i) {
        // mChats.add(chat);
        // for (int e=1;e<i+1;e++) {
        Chat chatseen = mChats.get(mChats.size() - 1);
        chatseen.setSeen(true);
       // mChats.remove(mChats.size() - 1);
       //    notifyItemRemoved(mChats.size() - 1);
        //  mChats.add(chatseen);
         notifyItemChanged(mChats.size() - 1);
        //notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        Chat chat = mChats.get(position);

        String alphabet = chat.sender.substring(0, 1);

        myChatViewHolder.txtChatMessage.setText(chat.message);
        myChatViewHolder.txtUserAlphabet.setText(alphabet);
        if (chat.getSeen()) {
            myChatViewHolder.Sent_seen_tv.setText("Seen");

        } else if (chat.getSent() && !chat.getSeen()) {
            myChatViewHolder.Sent_seen_tv.setText("Sent");
        }

        long time = chat.timestamp;
        String time_messge = convertTime(time);
        String real_time = "";
        String hour = time_messge.substring(0, 2);
        String minute = time_messge.substring(3, 5);
        String PmOrAm = time_messge.substring(6);
        if (Integer.valueOf(hour) < 12) {
            real_time = time_messge;

        } else {
            int h = Integer.valueOf(hour) - 12;
            real_time = h + ":" + minute + " " + PmOrAm;
        }
        myChatViewHolder.time_txt.setText(real_time);
    }

    public String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("HH:mm aa");
        return format.format(date);
    }

    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        Chat chat = mChats.get(position);

        String alphabet = chat.sender.substring(0, 1);

        otherChatViewHolder.txtChatMessage.setText(chat.message);
        otherChatViewHolder.txtUserAlphabet.setText(alphabet);
        long time = chat.timestamp;
        String time_messge = convertTime(time);
        String real_time = "";
        String hour = time_messge.substring(0, 2);
        String minute = time_messge.substring(3, 5);
        String PmOrAm = time_messge.substring(6);
        if (Integer.valueOf(hour) < 12) {
            real_time = time_messge;

        } else {
            int h = Integer.valueOf(hour) - 12;
            real_time = h + ":" + minute + " " + PmOrAm;
        }
        otherChatViewHolder.time_txt.setText(real_time);
    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet, time_txt, Sent_seen_tv;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            time_txt = (TextView) itemView.findViewById(R.id.time_txt);
            Sent_seen_tv = (TextView) itemView.findViewById(R.id.sent_seen_tv);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet, time_txt;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            time_txt = (TextView) itemView.findViewById(R.id.time_txt);

        }
    }


}
