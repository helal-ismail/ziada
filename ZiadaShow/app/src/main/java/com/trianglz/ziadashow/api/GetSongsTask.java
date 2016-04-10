package com.trianglz.ziadashow.api;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.trianglz.ziadashow.R;
import com.trianglz.ziadashow.ui.DetailsActivity;
import com.trianglz.ziadashow.ui.DrawerActivity;
import com.trianglz.ziadashow.ui.ParentActivity;
import com.trianglz.ziadashow.util.CustomAdapter;
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

public class GetSongsTask extends ApiAbstract {


    public GetSongsTask(Context mContext) {
        super(mContext);
        url = "http://www.bbc.co.uk/radio2/playlist.json";
        request_method ="GET";
        showDialog = true;
        dialogText = "Loading ... ";
    }

    @Override
    public void displayData(final ArrayList<SongItem> data) {

        ListView lv = (ListView) ((ParentActivity) mContext).findViewById(R.id.list);
        CustomAdapter adapter = new CustomAdapter((DrawerActivity)mContext, data);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                          Intent myIntent = new Intent(mContext, DetailsActivity.class);
                                          String SongItem = data.get(position).getTitle();
                                          String SongItem1 = data.get(position).getArtist();
                                          String SongItem2 = data.get(position).getImage();
                                          String SongItem3 = data.get(position).getList();

                                          myIntent.putExtra("title", SongItem);
                                          myIntent.putExtra("artist", SongItem1);
                                          myIntent.putExtra("image", SongItem2);
                                          myIntent.putExtra("playlist", SongItem3);
                                          mContext.startActivity(myIntent);
                                      }
                                  }
        );
    }


    @Override
    public ArrayList<SongItem> processData(String str) {
        ArrayList data = new ArrayList();
        try {
            final String RESULTS = "playlist";
            final String TITLE = "title";
            final String ARTIST = "artist";
            final String IMAGE = "image";
            final String List = "playlist";
            JSONObject obJson = new JSONObject(str);
            String[] labels = {"a", "b", "c", "aotw", "greatestriffs", "rotw"};
            JSONObject playlist = obJson.getJSONObject(RESULTS);

            for (int j = 0 ; j < labels.length ; j ++){
                JSONArray resultsArray = playlist.getJSONArray(labels[j]);
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject songs = resultsArray.getJSONObject(i);
                    SongItem item = new SongItem();
                    item.setTitle(songs.getString(TITLE));
                    item.setArtist(songs.getString(ARTIST));
                    item.setImage(songs.getString(IMAGE));
                    item.setList(songs.getString(List));
                    data.add(item);
                }
            }

        }
        catch (Exception e)
        {
            return data;
        }
        return data;
    }
}