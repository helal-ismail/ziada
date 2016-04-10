package com.trianglz.ziadashow.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class MasterActivity extends ParentActivity {
    ArrayList<SongItem> Data;
    ListView lv;
    Context context;
    private List<SongItem> songList = new ArrayList<SongItem>();
    private CustomAdapter adapter;
    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);


        context = this;
        Data = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
      //  adapter = new CustomAdapter(this, songList);
        lv.setAdapter(adapter);





        GetSongs player = new GetSongs();
        player.execute();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


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

                                      Intent myIntent = new Intent(MasterActivity.this,DetailsActivity.class);
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