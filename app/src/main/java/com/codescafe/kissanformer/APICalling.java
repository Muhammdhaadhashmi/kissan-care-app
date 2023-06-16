
package com.codescafe.kissanformer;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codescafe.kissanformer.Interface.Common;

import java.util.HashMap;
import java.util.Map;

public class APICalling {

    public static void Education_API(Activity activity, Common.APISuccessListener successListener, Common.APIErrorListener errorListener) {
        String url= "https://kissancare.reedspak.org/geteducation.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("recharge_history_res", response);
                successListener.onSuccessReceived(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.parseVolleyError(error, activity);
                Log.e("onErrorResponse", error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("id","7");
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(request);
    }
}
