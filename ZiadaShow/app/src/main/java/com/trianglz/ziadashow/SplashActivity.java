package com.trianglz.ziadashow;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;


public class SplashActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "VWqGIoLcmSaNP2Trba7Qq0Kpg";
    private static final String TWITTER_SECRET = "uUGRtStvPC9QkSC57ErLkKQqCPEoTRAbdJ0W0RPVq57AIJAymL";





    private final int SPLASH_DISPLAY_LENGTH = 2000;
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_splash);

    new Handler().postDelayed(new Runnable() {
        public void run() {

            Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }

    }, SPLASH_DISPLAY_LENGTH);
        }
        }