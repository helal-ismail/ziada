package com.trianglz.ziadashow;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.content.SharedPreferences;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.service.media.MediaBrowserService;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;



import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
public class LoginActivity extends FragmentActivity
         {


    private CallbackManager callbackManager;
    private static final String TWITTER_KEY = "VWqGIoLcmSaNP2Trba7Qq0Kpg";
    private static final String TWITTER_SECRET = "uUGRtStvPC9QkSC57ErLkKQqCPEoTRAbdJ0W0RPVq57AIJAymL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);
        Button facebook=(Button)findViewById(R.id.fbb);

        SharedPreferences sharedPref = getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean x=sharedPref.getBoolean(AppConstants.IS_LOGIN, false); // getting boolean

        if(x==true) {
                      successs();
        }


Button twitt=(Button)findViewById(R.id.btn_login_tw);

        twitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetTwitterTokenTask(this).execute();
            }
        });





        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initfb();
            }
        });



    }


    private void initfb(){

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Bundle params = new Bundle();
                params.putString("fields", "id,email,gender,cover,picture.type(large)");
                loginResult.getAccessToken();

                new GraphRequest(com.facebook.AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                if (response != null) {
                                    try {
                                        JSONObject data = response.getJSONObject();
                                        if (data.has("picture")) {
                                            String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                            Profile profile = Profile.getCurrentProfile();
                                            String valve = profile.getName();
                                            SharedPreferences sharedPref = getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString(AppConstants.profname, valve);
                                            editor.putString(AppConstants.ProfPic, profilePicUrl);
                                            editor.putBoolean(AppConstants.IS_LOGIN, true);
                                            editor.commit();

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "User cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "Error on Login", Toast.LENGTH_LONG).show();
            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        successs();
    }




    @Override
    protected void onResume() {
        super.onResume();
        Button facebook=(Button)findViewById(R.id.fbb);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initfb();
            }
        });



    }






             public void callBackDataFromAsyncTask(User user) {
//                 userId.setText(String.valueOf(user.getId()));
//                 userName.setText(user.getName());
//                 dataLayout.setVisibility(View.VISIBLE);
//                 btnLoginTwitter.setVisibility(View.GONE);

                 //loading User Avatar by Picasso

             }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public void successs(){
        Intent i =new Intent(this,DrawerActivity.class);
        startActivity(i);

    }

    public static Bitmap getFacebookProfilePicture(String url) throws IOException {
        URL facebookProfileURL= new URL(url);
        Bitmap bitmap = BitmapFactory.decodeStream(facebookProfileURL.openConnection().getInputStream());
        return bitmap;
    }

}