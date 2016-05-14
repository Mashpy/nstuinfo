package com.mashpy.nstuinfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

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

        //Texting
        String file_name = "json_string";

        String html = new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name);

        //detatils_wv.loadDataWithBaseURL(null, html, "text/html", "utf-8", "about:blank");
       // URLtv.setText(Html.fromHtml(html));
        URLtv.setText(html);
        // Making url clickable
        URLtv.setMovementMethod(LinkMovementMethod.getInstance());
        URLtv.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Store Data into file
     */
    public class ReadWriteJsonFileUtils {
        Activity activity;
        Context context;

        public ReadWriteJsonFileUtils(Context context) {

            this.context = context;

        }

        public void createJsonFileData(String filename, String mJsonResponse) {
            try {
                File checkFile = new File(context.getApplicationInfo().dataDir + "/new_directory_name/");
                if (!checkFile.exists()) {
                    checkFile.mkdir();
                }
                FileWriter file = new FileWriter(checkFile.getAbsolutePath() + "/" + filename);
                file.write(mJsonResponse);
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public String readJsonFileData(String filename) {
            try {
                File f = new File(context.getApplicationInfo().dataDir + "/new_directory_name/" + filename);
                if (!f.exists()) {
                    // onNoResult();
                    return null;
                }
                FileInputStream is = new FileInputStream(f);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                return new String(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // onNoResult();
            return null;
        }

        public void deleteFile() {
            File f = new File(context.getApplicationInfo().dataDir + "/new_directory_name/");
            File[] files = f.listFiles();
            for (File fInDir : files) {
                fInDir.delete();
            }
        }

        public void deleteFile(String fileName) {
            File f = new File(context.getApplicationInfo().dataDir + "/new_directory_name/" + fileName);
            if (f.exists()) {
                f.delete();
            }
        }
    }

}
