package com.trianglz.ziadashow.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.trianglz.ziadashow.ui.ParentActivity;
import com.trianglz.ziadashow.util.SongItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public abstract class ApiAbstract extends AsyncTask<Void, Void, String>{

    Context mContext;
    String url;
    String request_method;
    String dialogText;
    boolean showDialog;
    ProgressDialog pDialog;





    public ApiAbstract(Context mContext){
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showDialog)
        {
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage(dialogText);
            pDialog.show();
        }
        else
        {
            Toast.makeText(mContext, dialogText, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ArrayList<SongItem> data = processData(s);
        displayData(data);
        if(pDialog !=null)
            pDialog.hide();

    }


    public abstract void displayData(final ArrayList<SongItem> data);
    public abstract ArrayList<SongItem> processData(String str);

    @Override
    protected String doInBackground(Void... params) {
        String result = "";
        try
        {
            java.net.URL url_conn = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url_conn.openConnection();
            urlConnection.setRequestMethod(request_method);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            result = convertStreamToString(inputStream);

        }
        catch(Exception e){
            return "";
        }
        Log.v("Songs json string ", result);
        return result;
    }

    private String convertStreamToString(InputStream inputStream){
        try {
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return "";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return "";
            }
            return buffer.toString();
        }
        catch (Exception e) {
            return "";
        }
    }
}
