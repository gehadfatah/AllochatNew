package com.crazyhitty.chdev.ks.firebasechat.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.groupChat.GroupChatContract;
import com.crazyhitty.chdev.ks.firebasechat.ui.fragments.ChatFragment;
import com.crazyhitty.chdev.ks.firebasechat.ui.fragments.UsersFragment;
import com.crazyhitty.chdev.ks.firebasechat.ui.fragments.group_chat;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;

/**
 * Author: Kartik Sharma
 * Created on: 9/4/2016 , 12:03 PM
 * Project: FirebaseChat
 */

public class UserListingPagerAdapter extends FragmentPagerAdapter {
   static Context context;

    private static final Fragment[] sFragments = new Fragment[]{group_chat.newInstance(context ),
            UsersFragment.newInstance(UsersFragment.TYPE_One_chat)};
    private static final String[] sTitles = new String[]{"Group Chat",
            "Friends"};
    FragmentManager fragmentManager;
    public UserListingPagerAdapter(FragmentManager fm, Context context) {

        super(fm);
        this.context=context;
    }
/*

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // set the register screen fragment
        String tag = "";
        Fragment fragment = getItem(position);
        if (fragment == UsersFragment.newInstance(UsersFragment.TYPE_One_chat)) {
            tag=ChatFragment.class.getSimpleName();
        }else {
            tag=group_chat.class.getSimpleName();
        }
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_chat,
                fragment,
                tag);
        fragmentTransaction.commit();
        LayoutInflater inflater = LayoutInflater.from(get);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.id.frame_layout_content_chat, container, false);
        container.addView(layout);
        return layout;
//         View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        // Find and populate data into the page (i.e set the image)
       // ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        // ...
        // Add the page to the container
//        container.addView(itemView);
        // Return the page
//        return itemView;

      //  ModelObject modelObject = ModelObject.values()[position];
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
       // collection.addView(layout);
       // return layout;
    }
*/

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public Fragment getItem(int position) {
        return sFragments[position];
    }

    @Override
    public int getCount() {
        return sFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return sTitles[position];
    }
}
