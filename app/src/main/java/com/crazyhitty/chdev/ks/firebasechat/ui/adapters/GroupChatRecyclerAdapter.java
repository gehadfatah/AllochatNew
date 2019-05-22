package com.crazyhitty.chdev.ks.firebasechat.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.models.FriendlyMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 10/16/2016 , 10:36 AM
 * Project: FirebaseChat
 */

public class GroupChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<FriendlyMessage> mFriendlyMessages;

    public GroupChatRecyclerAdapter(List<FriendlyMessage> mFriendlyMessage) {
        mFriendlyMessages = mFriendlyMessage;
    }

    public void add(FriendlyMessage friendlyMessage) {
        mFriendlyMessages.add(friendlyMessage);
        notifyItemInserted(mFriendlyMessages.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_messages_from_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_messages_fromother, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TextUtils.equals(mFriendlyMessages.get(position).getEmail_sender(),
                FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        FriendlyMessage friendlyMessage = mFriendlyMessages.get(position);

        String alphabet = friendlyMessage.getEmail_sender().substring(0, 1);
        String email = friendlyMessage.getEmail_sender();

        myChatViewHolder.txtUserAlphabet.setText(alphabet);
        myChatViewHolder .txtUserEmail.setText(email);
        long time=friendlyMessage.timestamp;
        String time_messge=convertTime(time);
        String real_time="";
        String hour = time_messge.substring(0, 2);
        String minute = time_messge.substring(3, 5);
        String PmOrAm = time_messge.substring(6);
        if (Integer.valueOf(hour) < 12) {
            real_time=time_messge;

        }else {
            int h=Integer.valueOf(hour)-12;
            real_time = h + ":" + minute + " " + PmOrAm;
        }
        myChatViewHolder.time_txt.setText(real_time);
        boolean isPhoto = !friendlyMessage.getPhotoUrl().equals("");
        if (isPhoto) {
            myChatViewHolder. txtChatMessage.setVisibility(View.GONE);
            myChatViewHolder. photoImageView.setVisibility(View.VISIBLE);
            Picasso.with(myChatViewHolder.photoImageView.getContext())
                    .load(friendlyMessage.getPhotoUrl())
                    .placeholder(R.drawable.ic_messaging)
                    .into(myChatViewHolder. photoImageView);
        } else {
            myChatViewHolder.txtChatMessage.setVisibility(View.VISIBLE);
            myChatViewHolder. photoImageView.setVisibility(View.GONE);
            myChatViewHolder.txtChatMessage.setText(friendlyMessage.getText());

        }

    }
    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("HH:mm aa");
        return format.format(date);
    }
    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        FriendlyMessage freFriendlyMessage = mFriendlyMessages.get(position);

        String alphabet = freFriendlyMessage.getEmail_sender().substring(0, 1);
        String email = freFriendlyMessage.getEmail_sender();

        otherChatViewHolder.txtUserAlphabet.setText(alphabet);
        otherChatViewHolder.txtUserEmail.setText(email);
        boolean isPhoto = !freFriendlyMessage.getPhotoUrl().equals("");
        long time=freFriendlyMessage.timestamp;
        String time_messge=convertTime(time);
        String real_time="";
        String hour = time_messge.substring(0, 2);
        String minute = time_messge.substring(3, 5);
        String PmOrAm = time_messge.substring(6);
        if (Integer.valueOf(hour) < 12) {
            real_time=time_messge;

        }else {
            int h=Integer.valueOf(hour)-12;
            real_time = h + ":" + minute + " " + PmOrAm;
        }
        otherChatViewHolder.time_txt.setText(real_time);
        if (isPhoto) {
            otherChatViewHolder. txtChatMessage.setVisibility(View.GONE);
            otherChatViewHolder. photoImageView.setVisibility(View.VISIBLE);
            Glide.with(otherChatViewHolder.photoImageView.getContext())
                    .load(freFriendlyMessage.getPhotoUrl())
                    .placeholder(R.drawable.ic_messaging)
                    .into(otherChatViewHolder. photoImageView);
        } else {
            otherChatViewHolder.txtChatMessage.setVisibility(View.VISIBLE);
            otherChatViewHolder. photoImageView.setVisibility(View.GONE);
            otherChatViewHolder.txtChatMessage.setText(freFriendlyMessage.getText());

        }
    }

    @Override
    public int getItemCount() {
        if (mFriendlyMessages != null) {
            return mFriendlyMessages.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mFriendlyMessages.get(position).getEmail_sender(),
                FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet,txtUserEmail,time_txt;
        private ImageView photoImageView;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            txtUserEmail = (TextView) itemView.findViewById(R.id.text_view_user_email);
            photoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
            time_txt= (TextView) itemView.findViewById(R.id.time_txt);

        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet,txtUserEmail,time_txt;
        private ImageView photoImageView;
        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserEmail = (TextView) itemView.findViewById(R.id.text_view_user_email);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
             photoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
            time_txt= (TextView) itemView.findViewById(R.id.time_txt);

        }
    }
}
