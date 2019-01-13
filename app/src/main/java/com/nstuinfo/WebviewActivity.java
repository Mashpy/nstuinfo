package com.nstuinfo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nstuinfo.R;
import com.nstuinfo.mOtherUtils.Preferences;
import com.nstuinfo.mViews.FontAppearance;

public class WebviewActivity extends AppCompatActivity {

    private RelativeLayout rootRL;
    private LinearLayout btnLL;
    private Toolbar toolbar;
    private TextView appBarTitleTV;
    private WebView webView;
    private ProgressDialog progressDialog;
    private ImageButton btnBackward, btnForward;
    private static final String URL = "http://nstu.edu.bd/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.activity_webview);

        initViews();

        renderWebPage(URL);

        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoForward()) {
                    webView.goForward();
                }
            }
        });

        setTheme();

        FontAppearance.replaceDefaultFont(this);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        appBarTitleTV = findViewById(R.id.appBarTitleTV);
        appBarTitleTV.setText("nstu.edu.bd");

        rootRL = findViewById(R.id.RL);
        btnLL = findViewById(R.id.btnLL);
        btnLL.setVisibility(View.INVISIBLE);

        webView = findViewById(R.id.webView);
        btnBackward = findViewById(R.id.backwardBtn);
        btnForward = findViewById(R.id.forwardBtn);
    }

    // Custom method to render a web page
    @SuppressLint("SetJavaScriptEnabled")
    protected void renderWebPage(String urlToRender) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (webView.canGoBack()) {
                    btnLL.setVisibility(View.VISIBLE);
                    btnBackward.setVisibility(View.VISIBLE);
                } else {
                    btnBackward.setVisibility(View.INVISIBLE);
                }

                if (webView.canGoForward()) {
                    btnLL.setVisibility(View.VISIBLE);
                    btnForward.setVisibility(View.VISIBLE);
                } else {
                    btnForward.setVisibility(View.INVISIBLE);
                }

            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });

        webView.loadUrl(urlToRender);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
    }

    private void setTheme() {
        if (Preferences.isDarkTheme(this)) {
            rootRL.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
            btnLL.setBackgroundColor(Color.parseColor("#66000000"));
            toolbar.setBackgroundColor(Color.BLACK);
            toolbar.setPopupTheme(R.style.PopupMenuDark);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home){
            finish();
        } else if (id == R.id.menu_item_open_external){
            Uri uri = Uri.parse(URL);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
