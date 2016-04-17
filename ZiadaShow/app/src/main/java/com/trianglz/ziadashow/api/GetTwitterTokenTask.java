package com.trianglz.ziadashow.api;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.trianglz.ziadashow.R;
import com.trianglz.ziadashow.core.AppConstants;
import com.trianglz.ziadashow.ui.DrawerActivity;
import com.trianglz.ziadashow.ui.LoginActivity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;



public class GetTwitterTokenTask extends AsyncTask<String, Void, String> {

    public LoginActivity activity;
    private String oauthURL, verifier;
    private Dialog dialog;
    private WebView webView;
    ProgressDialog progressBar;

    // Twitter variables
    private static Twitter twitter;
    private static RequestToken requestToken;
    private static AccessToken accessToken;
    private static final String TWITTER_CONSUMER_KEY = "VWqGIoLcmSaNP2Trba7Qq0Kpg";
    private static final String TWITTER_CONSUMER_SECRET = "uUGRtStvPC9QkSC57ErLkKQqCPEoTRAbdJ0W0RPVq57AIJAymL";

    public GetTwitterTokenTask(LoginActivity activity) {
        this.activity =  activity;
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //showing a progress dialog
        progressBar = new ProgressDialog(activity);
        progressBar.setMessage("Connecting...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        progressBar.show();
    }

    @Override
    protected String doInBackground(String... args) {

        try {
            requestToken = twitter.getOAuthRequestToken();
            oauthURL = requestToken.getAuthorizationURL();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return oauthURL;
    }

    @Override
    protected void onPostExecute(String oauthUrl) {
        if (oauthUrl != null) {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.auth_dialog);
            webView = (WebView) dialog.findViewById(R.id.webv);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(oauthUrl);

            webView.setWebViewClient(new WebViewClient() {
                boolean authComplete = false;

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (url.contains("oauth_verifier") && authComplete == false) {
                        authComplete = true;
                        Log.e("AsyncTask", url);
                        Uri uri = Uri.parse(url);
                        verifier = uri.getQueryParameter("oauth_verifier");

                        dialog.dismiss();

                        //revoke access token asynctask
                        new AccessTokenGetTask().execute();
                    } else if (url.contains("denied")) {
                        dialog.dismiss();
                        Toast.makeText(activity, "Permission is Denied", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
            dialog.setCancelable(true);
            progressBar.dismiss(); //dismiss progress dialog when task finished.
        } else {
            Toast.makeText(activity, "Network Error!", Toast.LENGTH_SHORT).show();
        }
    }



    public class AccessTokenGetTask extends AsyncTask<String, String, User > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressDialog(activity);
            progressBar.setMessage("Fetching Data ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setCancelable(false);
            progressBar.show();
        }

        @Override
        protected User doInBackground(String... args) {
            User user = null;
            try {
                accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
                user = twitter.showUser(accessToken.getUserId());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return user;
        }

        @Override
        protected void onPostExecute(User response) {
            if (response == null) {
                Log.e("AsyncTask", "null user");
            } else {
                //call back data to UI here
                callBackDataFromAsyncTask(response);

            }
            progressBar.dismiss();
            Intent i =new Intent(activity,DrawerActivity.class);
            activity.startActivity(i);
        }
    }


    private void callBackDataFromAsyncTask(User user) {
        String vacao=user.getBiggerProfileImageURL();
        String userName=user.getName();
        SharedPreferences sharedPref = activity.getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
                 editor.putString(AppConstants.profname, userName);
                 editor.putString(AppConstants.ProfPic, vacao);
                 editor.putBoolean(AppConstants.IS_LOGIN, true);
                 editor.putString(AppConstants.type,"twitter");
                 editor.commit();
    }

}