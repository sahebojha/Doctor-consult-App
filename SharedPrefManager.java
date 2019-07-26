package com.example.sahebojha.doctorconsult;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sahebojha on 1/5/2018.
 */

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String USERTYPE = "usertype";
    private static final String USEREMAIL = "email";
    private static final String USERID = "id";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id, String email, String usertype) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(USERID, id);
        editor.putString(USEREMAIL, email);
        editor.putString(USERTYPE, usertype);

        editor.apply();

        return true;
    }

    public String getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(USERTYPE, null) != null) {
            return sharedPreferences.getString(USERTYPE, null);
        }
        return null;
    }

    public int isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getInt(USERID, 0) != 0) {
            return sharedPreferences.getInt(USERID, 0);
        }
        return 0;
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
