package com.mwbtech.dealer_register.Dashboard.Support;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.mwbtech.dealer_register.R;

/**
 * Created by Prerna Sharma on 09-02-2023
 */
public class BusinessNewsActivity extends AppCompatActivity {
    // setting the TAG for debugging purposes
    private static final String TAG = "BusinessNewsActivity";

    // declaring the webview
    private WebView myWebView;
    private ImageView back;

    // declaring the url string variable
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businews_news);

        // assigning the views to id's
        myWebView = findViewById(R.id.webview);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        Intent intent = getIntent();

        // checking if there is an intent
        if (intent != null) {
            // retrieving the url in the intent
            url = intent.getStringExtra("url_key");

            // loading and displaying a web page in the activity
            myWebView.loadUrl(url);
        }

    }
/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, BusinessNewsListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        BusinessNewsActivity.this.finish();
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}