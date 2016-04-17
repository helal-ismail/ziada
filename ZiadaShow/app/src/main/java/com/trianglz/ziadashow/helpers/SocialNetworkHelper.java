package com.trianglz.ziadashow.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.trianglz.ziadashow.ui.DrawerActivity;

public abstract class SocialNetworkHelper{

    public static Context mContext;

    public abstract void initSocialNetwork();

    public static void successs(){
        Intent intent =new Intent(mContext,DrawerActivity.class);
        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }
}