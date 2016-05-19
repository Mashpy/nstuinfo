package com.mashpy.nstuinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        String file_name= getIntent().getStringExtra("root_path");

        try{

            String exp_date = getIntent().getStringExtra("exp");
            long date = System.currentTimeMillis();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            String current_date = formatter.format(date);
            Date cur_date1 = formatter.parse(current_date);
            Date exp_date2 = formatter.parse(exp_date);
           if (exp_date2.compareTo(cur_date1)<0)
            {
               open();//exp_date is less then current Date Current Date
            }

        }catch (ParseException e1){
            e1.printStackTrace();
        }

        String html = new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name);

        if(html==null)
        {
            detatils_wv.loadDataWithBaseURL(null, "Reload Please ", "text/html", "utf-8", "about:blank");
        }
        else
        {
            detatils_wv.loadDataWithBaseURL(null, html, "text/html", "utf-8", "about:blank");
        }
         // URLtv.setText(Html.fromHtml(html));
       // URLtv.setText(html);
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

    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Data Validity has been expired. Please Internet and press yes ");

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(DetailsActivity.this, "You clicked yes button", Toast.LENGTH_LONG).show();

            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
