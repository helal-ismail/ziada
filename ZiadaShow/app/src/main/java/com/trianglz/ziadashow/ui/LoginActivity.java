package com.trianglz.ziadashow.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.trianglz.ziadashow.R;
import com.trianglz.ziadashow.core.AppConstants;
import com.trianglz.ziadashow.helpers.FacebookHelper;
import com.trianglz.ziadashow.helpers.SocialNetworkHelper;
import com.trianglz.ziadashow.helpers.TwitterHelper;

public class LoginActivity extends FragmentActivity  {


    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookHelper.getInstance(this).initSocialNetwork();
        TwitterHelper.getInstance(this).initSocialNetwork();

        SharedPreferences sharedPref = getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean(AppConstants.IS_LOGIN, false); // getting boolean
        if(isLoggedIn) {
            SocialNetworkHelper.successs();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookHelper.getInstance(this).getCallBackManager().onActivityResult(requestCode, resultCode, data);
    }




}