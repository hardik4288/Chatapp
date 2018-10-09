package com.example.user.chitchat;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by user on 16/01/2018 AD.
 */

public class PagerAdapter extends FragmentPagerAdapter {


    public PagerAdapter(FragmentManager fm) {




        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
     switch (position) {
         case 0:
             ChatFragment chatFragment = new ChatFragment();
             return chatFragment;
         case 1:
             FriendsFragment friendsFragment = new FriendsFragment();
             return friendsFragment;

         default:
                 return null;

     }





    }

    @Override
    public int getCount() {

        return 2;
    }
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Chats";
            case 1:
                return "Friends";

            default:
                return null;
        }

    }
}
