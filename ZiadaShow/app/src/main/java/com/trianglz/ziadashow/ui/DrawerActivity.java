package com.trianglz.ziadashow.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import com.trianglz.ziadashow.api.GetSongsTask;
import com.trianglz.ziadashow.core.AppConstants;
import com.trianglz.ziadashow.util.CustomAdapter;
import com.trianglz.ziadashow.R;
import com.trianglz.ziadashow.util.SongItem;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends ParentActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<SongItem> data;
    ListView lv;
    private List<SongItem> songList = new ArrayList<SongItem>();
    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
        String picValue =sharedPref.getString(AppConstants.ProfPic, "");
        String nameValue = sharedPref.getString(AppConstants.profname, "");


        GetSongsTask getSongsTask = new GetSongsTask(this);
        getSongsTask.execute();




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
      //  View headerLayout = navigationView.getHeaderView(0);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_drawer);

        Button bb=(Button)headerLayout.findViewById(R.id.logouttwitt);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disconnectFromFacebook();
                SharedPreferences sharedPref = getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(AppConstants.IS_LOGIN, false);
                editor.commit();
                logout();
            }
        });

        ImageView  imag =(ImageView) headerLayout.findViewById(R.id.profpic);
       Picasso.with(this).load(picValue).into(imag);

        TextView tss=(TextView) headerLayout.findViewById(R.id.username);
        tss.setText(nameValue);

    }


//    public void logouttwitter(){
//
//        CookieSyncManager.createInstance(this);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.removeSessionCookie();
//        Twitter.getSessionManager().clearActiveSession();
//        Twitter.logOut();
//
//
//    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }








    public void logout(){


        Intent is=new Intent(this,LoginActivity.class);
        startActivity(is);
//        SharedPreferences sharedPref = getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.clear();
//        editor.commit();

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
