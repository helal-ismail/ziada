package com.trianglz.ziadashow;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;

import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<SongItem> Data;
    ListView lv;
    Context context;
    private List<SongItem> songList = new ArrayList<SongItem>();
    private CustomAdapter adapter;
    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        Data = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        adapter = new CustomAdapter(this, songList);
        lv.setAdapter(adapter);


        SharedPreferences sharedPref = getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
        String picValue = sharedPref.getString(AppConstants.ProfPic, "");
        String nameValue = sharedPref.getString(AppConstants.profname, "");


        GetSongs player = new GetSongs();
        player.execute();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();



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

    private void getDataFromJson(String songsJsonStr)
            throws JSONException {


        final String RESULTS = "playlist";
        final String TITLE = "title";
        final String ARTIST = "artist";
        final String IMAGE = "image";
        final String List="playlist";
        JSONObject obJson = new JSONObject(songsJsonStr);
        JSONObject playlist = obJson.getJSONObject(RESULTS);
        JSONArray resultsArray = playlist.getJSONArray("a");
        JSONArray resultsArray1 = playlist.getJSONArray("b");
        JSONArray resultsArray2 = playlist.getJSONArray("c");
        JSONArray resultsArray3 = playlist.getJSONArray("aotw");
        JSONArray resultsArray4 = playlist.getJSONArray("greatestriffs");
        JSONArray resultsArray5 = playlist.getJSONArray("rotw");

        SongItem item;
        String title;
        String artist;
        String image;
        String list;

        for (int i = 0; i < resultsArray.length(); i++) {

            item = new SongItem();

            JSONObject songs = resultsArray.getJSONObject(i);

            title = songs.getString(TITLE);
            item.setTitle(title);
            artist = songs.getString(ARTIST);
            item.setArtist(artist);
            image = songs.getString(IMAGE);
            item.setImage(image);
            list=songs.getString(List);
            item.setList(list);
            SongItem ss=new SongItem();
            ss.setArtist(songs.getString(ARTIST));
            ss.setImage(songs.getString(IMAGE));
            ss.setTitle(songs.getString(TITLE));
            ss.setList(songs.getString(List));
            songList.add(ss);
            Data.add(item);
            hidePDialog();

        }

        for (int i = 0; i < resultsArray1.length(); i++) {

            item = new SongItem();

            JSONObject songs = resultsArray1.getJSONObject(i);

            title = songs.getString(TITLE);
            item.setTitle(title);
            artist = songs.getString(ARTIST);
            item.setArtist(artist);
            image = songs.getString(IMAGE);
            item.setImage(image);

            SongItem ss=new SongItem();
            ss.setArtist(songs.getString(ARTIST));
            ss.setImage(songs.getString(IMAGE));
            ss.setTitle(songs.getString(TITLE));
            songList.add(ss);
            Data.add(item);

        }
        for (int i = 0; i < resultsArray2.length(); i++) {

            item = new SongItem();

            JSONObject songs = resultsArray2.getJSONObject(i);
            title = songs.getString(TITLE);
            item.setTitle(title);
            artist = songs.getString(ARTIST);
            item.setArtist(artist);
            image = songs.getString(IMAGE);
            item.setImage(image);

            Data.add(item);
        }
        for (int i = 0; i < resultsArray3.length(); i++) {

            item = new SongItem();

            JSONObject songs = resultsArray3.getJSONObject(i);
            title = songs.getString(TITLE);
            item.setTitle(title);
            artist = songs.getString(ARTIST);
            item.setArtist(artist);
            image = songs.getString(IMAGE);
            item.setImage(image);

            SongItem ss=new SongItem();
            ss.setArtist(songs.getString(ARTIST));
            ss.setImage(songs.getString(IMAGE));
            ss.setTitle(songs.getString(TITLE));
            songList.add(ss);
            Data.add(item);
        }
        for (int i = 0; i < resultsArray4.length(); i++) {

            item = new SongItem();

            JSONObject songs = resultsArray4.getJSONObject(i);
            title = songs.getString(TITLE);
            item.setTitle(title);
            artist = songs.getString(ARTIST);
            item.setArtist(artist);
            image = songs.getString(IMAGE);
            item.setImage(image);

            SongItem ss=new SongItem();
            ss.setArtist(songs.getString(ARTIST));
            ss.setImage(songs.getString(IMAGE));
            ss.setTitle(songs.getString(TITLE));
            songList.add(ss);


            Data.add(item);

        }
        for (int i = 0; i < resultsArray5.length(); i++) {

            item = new SongItem();

            JSONObject songs = resultsArray5.getJSONObject(i);
            title = songs.getString(TITLE);
            item.setTitle(title);
            artist = songs.getString(ARTIST);
            item.setArtist(artist);
            image = songs.getString(IMAGE);
            item.setImage(image);

            SongItem ss=new SongItem();
            ss.setArtist(songs.getString(ARTIST));
            ss.setImage(songs.getString(IMAGE));
            ss.setTitle(songs.getString(TITLE));
            songList.add(ss);
            Data.add(item);
        }

    }

    public class GetSongs extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean result) {
            if (result=true){

                lv = (ListView) findViewById(R.id.list);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                              @Override
                                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                  Intent myIntent = new Intent(DrawerActivity.this,DetailsActivity.class);
                                                  String SongItem = Data.get(position).getTitle();
                                                  String SongItem1 = Data.get(position).getArtist();
                                                  String SongItem2 = Data.get(position).getImage();
                                                  String SongItem3= Data.get(position).getList();

                                                  myIntent.putExtra("title", SongItem);
                                                  myIntent.putExtra("artist", SongItem1);
                                                  myIntent.putExtra("image", SongItem2);
                                                  myIntent.putExtra("playlist",SongItem3);
                                                  startActivity(myIntent);




                                              }
                                          }
                );



            }

        }

        protected Boolean doInBackground(String... params) {

            Boolean result = false;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String songsJsonStr = null;


            try {

                URL url = new URL("http://www.bbc.co.uk/radio2/playlist.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return result;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return result;
                }
                songsJsonStr = buffer.toString();
                Log.v("Songs json string ", songsJsonStr);

            } catch (Exception e) {

                Log.e("connection", "Error ", e);

                return result;
            }



            try

            {
                getDataFromJson(songsJsonStr);
                result = true;
                return result;
            } catch (JSONException e) {

                Log.e("ayyyyyyyy", e.getMessage(), e);
                e.getStackTrace();
            }
            return result;
        }
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
