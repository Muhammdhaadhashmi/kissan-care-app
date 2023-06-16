package com.codescafe.kissanformer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class Utils {
    public static String server_key="key=AAAAqWjm72o:APA91bE52IwGb9K3q1jOymXzbcl_0iWazMQhJe-bqUInP14_2AG8CfgsmJlKsH9hdomH6vYNWTPN2cKNrqm9dYTfigapAEL-wcE4XciOdJn8edASOismeviZ4gxGIK9MBSNgT4SweTc8";
    public static void parseVolleyError(VolleyError error, Context context) {
        String json;
        NetworkResponse response = error.networkResponse;
        if (response != null && response.data != null) {
            try {
                JSONObject errorObj = new JSONObject(new String(response.data));
                if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                    try {
                        setToast((Activity) context,errorObj.optString("message"));
                        Log.e("eeee_log",errorObj.optString("message"));
                    } catch (Exception e) {
                        setToast((Activity) context,context.getString(R.string.something_went_wrong));
                        Log.e("eeee_log",e.toString());
                    }
                } else if (response.statusCode == 401) {
                    try {
                        if (!errorObj.optString("message").equalsIgnoreCase("invalid_token")) {
                            setToast((Activity) context,errorObj.optString("message"));
                        }
                    } catch (Exception e) {
                        setToast((Activity) context,context.getString(R.string.something_went_wrong));
                        Log.e("eeee_log",e.toString());
                    }
                } else if (response.statusCode == 422) {
                    json = trimMessage(new String(response.data));
                    if (json != null && !json.equals("")) {
                        setToast((Activity) context,json.toString());
                    } else {
                        setToast((Activity) context,context.getString(R.string.please_try_again));
                    }
                } else {
                    setToast((Activity) context,context.getString(R.string.please_try_again));
                }
            } catch (Exception e) {
                setToast((Activity) context,context.getString(R.string.something_went_wrong));
                Log.e("eeee_log",e.toString());
            }
        } else {
            if (error instanceof NoConnectionError) {
                setToast((Activity) context,context.getString(R.string.oops_connect_your_internet));
            } else if (error instanceof NetworkError) {
                setToast((Activity) context,context.getString(R.string.oops_connect_your_internet));
            } else if (error instanceof TimeoutError) {
                setToast((Activity) context,context.getString(R.string.something_went_wrong));
                Log.e("eeee_log","timeout");
            }
        }
    }
    public static String trimMessage(String json) {
        StringBuilder trimmedString = new StringBuilder();

        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iter = jsonObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    JSONArray value = jsonObject.getJSONArray(key);
                    for (int i = 0, size = value.length(); i < size; i++) {
                        Log.e("Errors in Form", "" + value.getString(i));
                        trimmedString.append(value.getString(i));
                        if (i < size - 1) {
                            trimmedString.append('\n');
                        }
                    }
                } catch (JSONException e) {

                    trimmedString.append(jsonObject.optString(key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        Log.e("Trimmed", "" + trimmedString);

        return trimmedString.toString();
    }
    public static void setToast(Activity context, String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
