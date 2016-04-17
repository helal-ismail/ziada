package com.trianglz.ziadashow.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.trianglz.ziadashow.R;
import com.trianglz.ziadashow.core.AppConstants;
import com.trianglz.ziadashow.ui.LoginActivity;

import org.json.JSONObject;

import java.util.Arrays;

public class FacebookHelper extends SocialNetworkHelper {

    private CallbackManager callBackManager;
    private static FacebookHelper instance = new FacebookHelper();
    private String name = "";


    public static FacebookHelper getInstance(Context context) {
        mContext = context;
        return instance;
    }

    @Override
    public void initSocialNetwork() {
        FacebookSdk.sdkInitialize(mContext.getApplicationContext());
        Button facebook = (Button) ((LoginActivity) mContext).findViewById(R.id.fbb);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFb();
            }
        });
    }

    public CallbackManager getCallBackManager(){
        return callBackManager;
    }

    private void initFb(){
        callBackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions((Activity)mContext, Arrays.asList("public_profile", "user_friends"));
        LoginManager.getInstance().registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                if(Profile.getCurrentProfile() == null) {
                    ProfileTracker mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            name = profile2.getName();
                            this.stopTracking();
                            Profile.setCurrentProfile(profile2);
                        }
                    };
                    mProfileTracker.startTracking();
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    name = profile.getName();
                }

                Log.d("helal","Login Result token : " + loginResult.getAccessToken().getToken());
                Log.d("helal", "Another Token : " + com.facebook.AccessToken.getCurrentAccessToken().getToken());
                Bundle params = new Bundle();
                params.putString("fields", "id,email,gender,cover,picture.type(large)");
              // loginResult.getAccessToken();

                new GraphRequest(com.facebook.AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                if (response != null) {
                                    try {



                                        JSONObject data = response.getJSONObject();
                                        Log.d("helal", data.toString());
                                        if (data.has("picture")) {
                                            String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");

                                            SharedPreferences sharedPref = mContext.getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString(AppConstants.profname, name);
                                            editor.putString(AppConstants.ProfPic, profilePicUrl);
                                            editor.putBoolean(AppConstants.IS_LOGIN, true);
                                            editor.putString(AppConstants.type,"facebook");
                                            editor.commit();
                                            successs();

                                        }
                                    } catch (Exception e) {
                                        Log.d("helal", e.toString());
                                    }

                                }
                            }
                        }).executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext, "User cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(mContext, "Error on Login", Toast.LENGTH_LONG).show();
            }
        });
        ((LoginActivity)mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}