package com.trianglz.ziadashow.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import com.trianglz.ziadashow.core.AppConstants;

public class ParentActivity extends AppCompatActivity{

    SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        sharedPref = getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);

    }

}
