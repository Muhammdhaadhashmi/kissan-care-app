package com.codescafe.kissanformer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.model.AllMediaModel;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DatabaseReference;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Observable;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

public class PdfViewer extends AppCompatActivity {

    String url = "https://kissancare.reedspak.org/upload/pfds/";
    String title;

    PDFView pdfView;
    WebView webView;
    AllMediaModel allMediaModel;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        allMediaModel = (AllMediaModel) getIntent().getSerializableExtra("url");

        pdfView = findViewById(R.id.pdfView);

        webView = findViewById(R.id.webview);
        //webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl(url+al());
        pdfOpen(url+allMediaModel.getFar_src());
    }

    private void pdfOpen(String fileUrl){

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        //---you need this to prevent the webview from
        // launching another browser when a url
        // redirection occurs---
        webView.setWebViewClient(new Callback());

        webView.loadUrl(
                "http://docs.google.com/gview?embedded=true&url=" + fileUrl);

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }
//    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
//        @Override
//        protected InputStream doInBackground(String... strings) {
//            // we are using inputstream
//            // for getting out PDF.
//            InputStream inputStream = null;
//            try {
//                URL url = new URL(strings[0]);
//                // below is the step where we are
//                // creating our connection.
//                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
//                if (urlConnection.getResponseCode() == 200) {
//                    // response is success.
//                    // we are getting input stream from url
//                    // and storing it in our variable.
//                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
//                }
//
//            } catch (IOException e) {
//                // this is the method
//                // to handle errors.
//                e.printStackTrace();
//                return null;
//            }
//            return inputStream;
//        }
//
//        @Override
//        protected void onPostExecute(InputStream inputStream) {
//            // after the execution of our async
//            // task we are loading our pdf in our pdf view.
//            Log.e("pdf", "show");
//            pdfView.fromStream(inputStream).load();
//        }
//    }
//
//    class RetrievePDFbyte extends AsyncTask<String, Void, byte[]> {
//        ProgressDialog progressDialog;
//
//        protected void onPreExecute() {
////            progressDialog = new ProgressDialog(activity);
////            progressDialog.setTitle("getting the book content...");
////            progressDialog.setMessage("Please wait...");
////            progressDialog.setCanceledOnTouchOutside(false);
////            progressDialog.show();
//
//        }
//
//        @Override
//        protected byte[] doInBackground(String... strings) {
//            InputStream inputStream = null;
//            try {
//                URL url = new URL(strings[0]);
//                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
//                if (httpsURLConnection.getResponseCode() == 200) {
//                    inputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
//                }
//            } catch (IOException e) {
//                return null;
//            }
//            try {
//                return IOUtils.toByteArray(inputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(byte[] bytes) {
//            pdfView.fromBytes(bytes).load();
//        }
//
//    }

}