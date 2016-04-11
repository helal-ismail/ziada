package com.trianglz.ziadashow.helpers;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.trianglz.ziadashow.R;
import com.trianglz.ziadashow.api.GetTwitterTokenTask;
import com.trianglz.ziadashow.helpers.SocialNetworkHelper;
import com.trianglz.ziadashow.ui.LoginActivity;

public class TwitterHelper extends SocialNetworkHelper{

    private static TwitterHelper instance = new TwitterHelper();

    public static TwitterHelper getInstance(Context context) {
        mContext = context;
        return instance;
    }

    @Override
    public void initSocialNetwork() {

        Button twitt=(Button)((LoginActivity)mContext).findViewById(R.id.btn_login_tw);

        twitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetTwitterTokenTask((LoginActivity)mContext).execute();
            }
        });

    }
}