package com.codescafe.kissanformer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.model.AllMediaModel;

public class VideoView extends AppCompatActivity {

    String allMediaModel;
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        allMediaModel = getIntent().getStringExtra("url");

        //Toast.makeText(this, ""+allMediaModel, Toast.LENGTH_SHORT).show();
        webview = findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(allMediaModel);

    }
}