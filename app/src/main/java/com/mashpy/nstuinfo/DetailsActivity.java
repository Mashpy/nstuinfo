package com.mashpy.nstuinfo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaitls);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView URLtv = (TextView) findViewById(R.id.urltv);
        WebView detatils_wv = (WebView) findViewById(R.id.webview);
        detatils_wv.getSettings().setJavaScriptEnabled(true);
        String UrlFrom_MainActivity = getIntent().getStringExtra("url");
        detatils_wv.loadDataWithBaseURL(null, UrlFrom_MainActivity, "text/html", "utf-8", "about:blank");
        URLtv.setText(Html.fromHtml(UrlFrom_MainActivity));
        // Making url clickable
        URLtv.setMovementMethod(LinkMovementMethod.getInstance());
        URLtv.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
