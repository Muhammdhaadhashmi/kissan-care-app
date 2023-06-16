package com.codescafe.kissanformer.Interface;

import com.android.volley.VolleyError;

public interface Common {

    interface OTPListener {
        void onOTPReceived(String otp);
    }

    interface APISuccessListener {
        void onSuccessReceived(String success);
    }
    interface APIErrorListener {
        void onErrorReceived(VolleyError error);
    }
}
