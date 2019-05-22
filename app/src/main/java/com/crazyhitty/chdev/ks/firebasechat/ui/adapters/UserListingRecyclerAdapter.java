package com.crazyhitty.chdev.ks.firebasechat.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 2:23 PM
 * Project: FirebaseChat
 */

public class UserListingRecyclerAdapter extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder> {
    private List<User> mUsers;
    String staatus;

    public UserListingRecyclerAdapter(List<User> users) {
        this.mUsers = users;
    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_user_listing, parent, false);
        /*if (FirebaseAuth.getInstance().getCurrentUser() != null) {

               *//* boolean status = FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(Constants.ARG_USER_status)
                        .equalTo("true");*//*

            FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).
                    child(Constants.ARG_UID).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User  user ;
                    user=  dataSnapshot.getValue(User.class);
                    // setstatus(user.status);
                    staatus=user.status;
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

                }
            });


        }*/
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         User user = mUsers.get(position);
        try {
            String alphabet = user.email.substring(0, 1);
            // holder.txtUserAlphabet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0);
            String name = user.email.substring(0, user.email.indexOf("@"));
            holder.txtUsername.setText(name);
            holder.txtUserAlphabet.setText(alphabet);


        } catch (Exception e) {
            e.getMessage();
        }

        if (Boolean.valueOf(user.status)) {
            holder.status.setImageResource(R.drawable.ic_online);
           // holder.txtUserAlphabet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dk, 0,0 , 0);

        }else {
            holder.status.setImageResource(R.drawable.offline);
          //  holder.txtUserAlphabet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.offline, 0, 0, R.drawable.ic_online);
        }
    }

    private void setstatus(String status) {
        staatus=status;

    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public User getUser(int position) {
        return mUsers.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserAlphabet, txtUsername;
        ImageView status;
        ViewHolder(View itemView) {
            super(itemView);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_viewer_alphabet);
            txtUsername = (TextView) itemView.findViewById(R.id.text_view_username);
            status=(ImageView) itemView.findViewById(R.id.status);
        }
    }
}
