package com.codescafe.kissanformer.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.codescafe.kissanformer.model.DataEntryModel;
import com.codescafe.kissanformer.model.SupportTeamModel;
import com.google.gson.Gson;


public class DataEnterManager {

    private final SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    public static String intro = "intro";
    public static String login = "login";
    public static String isopen = "isopen";
    public static String user = "users";
    public static String dcharge = "dcharge";
    public static String address = "address";
    public static String wallet = "wallet";
    public static String istip = "tip";
    public static String tips = "tips";
    public static String istax = "tax";
    public static String taxs = "taxs";
    public static String walletname = "walletname";


    public static String currency = "currency";
    public static String pincode = "pincode";
    public static String pincoded = "pincoded";
    public static String coupon = "coupon";
    public static String couponid = "couponid";
    public static String storename = "storename";
    public static String restid = "restid";


    public static String terms = "terms";
    public static String contact = "contact";
    public static String about = "about";
    public static String policy = "policy";

    public DataEnterManager(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public void setStringData(String key, String val) {
        mEditor.putString(key, val);
        mEditor.commit();
    }

    public String getStringData(String key) {
        return mPrefs.getString(key, "");
    }


    public void setBooleanData(String key, Boolean val) {
        mEditor.putBoolean(key, val);
        mEditor.commit();
    }

    public boolean getBooleanData(String key) {
        return mPrefs.getBoolean(key, false);
    }

    public void setIntData(String key, int val) {
        mEditor.putInt(key, val);
        mEditor.commit();
    }

    public int getIntData(String key) {
        return mPrefs.getInt(key, 0);
    }

    public void setUserDetails(String key, DataEntryModel val) {
        mEditor.putString(user, new Gson().toJson(val));
        mEditor.commit();
    }

    public DataEntryModel getUserDetails(String key) {
        return new Gson().fromJson(mPrefs.getString(user, ""), DataEntryModel.class);
    }

    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }
}
