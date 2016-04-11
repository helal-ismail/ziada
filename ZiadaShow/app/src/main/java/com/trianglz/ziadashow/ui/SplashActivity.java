package com.trianglz.ziadashow.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.trianglz.ziadashow.R;



public class SplashActivity extends ParentActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

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