package com.mashpy.nstuinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public String expire_date;
    public boolean reload_status = true;
    public boolean dialog_status = true;
    public boolean update_status = true;
    public String SourceURL = "https://raw.githubusercontent.com/Mashpy/nstuinfo/develop/version.json";
    public int progressMax = 0;
    String jsonData = "";
    String ver ;
    /**
     * Json Files Name
     */
    String file_name = "json_string";
    /**
     * Internal Storage Directory Name
     */
    String directoryName = "nstuinfo";
    /**
     * Auto AsyncTask variables
     */
    int online_jasonObjectLenth = 0;
    int offline_jasonObjectLength  = 0;
    int downloadedItem = 0;
    JSONArray articles;
    JSONArray articles_previous;

    /**JsonDataList*/
    List<jsonDataList> OnlineJsonData = new ArrayList<>();
    List<jsonDataList> OfflineJsonData = new ArrayList<>();
    List<jsonDataList> UpdateJsonData = new ArrayList<>();
    List<jsonDataList> DeleteJsonData = new ArrayList<>();


    private List<RecyclerData> recyclerDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerDataAdapter mAdapter;
    private Document htmlDocument;
    private String htmlContentInStringFormat;
    private ProgressDialog progress;
    private ProgressDialog progressSpiner;

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * Parse Data Form Input Stream
     */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {

                    if (reload_status == true) {
                        circularProgressBar();
                        reload_status = false;
                        getUpdatedData();
                    } else {
                        Snackbar.make(view, "Wait Reload is Processing...", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(view, "Please turn on your data connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new RecyclerDataAdapter(recyclerDataList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                RecyclerData recyclerData = recyclerDataList.get(position);
                Intent details = new Intent(MainActivity.this, DetailsActivity.class);
                details.putExtra("root_path", recyclerData.getUrl());
                details.putExtra("exp", expire_date);
                startActivity(details);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        if (isConnected()) {
            prepareMovieData();
        } else {
            OffLineData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.about)
        {
            Intent i = new Intent(MainActivity.this, ImageViewUse.class);
            startActivity(i);
        }
        else if(id == R.id.clearAll)
        {
            new ReadWriteJsonFileUtils(getBaseContext()).deleteFile();
            recyclerDataList.clear();
            OffLineData();

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Check Internet Connection
     */
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void prepareMovieData() {

        getUpdatedData();
    }

    public void OffLineData() {
        String result;
        if (new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name) == null) {
            try {
                InputStream is = getAssets().open("json_directory/" + file_name);
                // We guarantee that the available method returns the total
                // size of the asset...  of course, this does mean that a single
                // asset can't be more than 2 gigs.
                int size = is.available();
                // Read the entire asset into a local byte buffer.
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                // Convert the buffer into a string.
                result = new String(buffer);
                try {
                    new offlineJsonFileUtils(getBaseContext()).createJsonFileData(file_name, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                // Should never happen!
                throw new RuntimeException(e);
            }
        }

        offlineHtml();

        try {

            TextView ShowVersion = (TextView) findViewById(R.id.versionName);

            String str = "";
            String jsonString = new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name);
            JSONObject json = new JSONObject(jsonString);
            JSONArray articles = json.getJSONArray("article_list");
            int jasonObjecLenth = json.getJSONArray("article_list").length();

            ver = (String) json.get("version");

            ShowVersion.setText("Version : 3.0 Data Version : "+ver);

            for (int i = 0; i < jasonObjecLenth; i++) {
                RecyclerData recyclerData = new RecyclerData(articles.getJSONObject(i).getString("menu_name"), articles.getJSONObject(i).getString("last_update"), "", articles.getJSONObject(i).getString("root_path"), articles.getJSONObject(i).getString("type"));
                recyclerDataList.add(recyclerData);
            }
            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void offlineHtml() {

        String result = "";
        String file_name[] = {"introduction", "regentboard", "academic_council", "committees", "register_office", "central_library", "dept_teacher", "cr", "academic_calender", "academic_officiary", "administrative", "student_activities", "transport_section", "hall_office", "emergency_contacts", "message_from_developer"};
        for (int i = 0; i < 16; i++) {

            if (new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name[i]) == null) {
                try {
                    InputStream is = getAssets().open("html_directory/" + file_name[i]);
                    // We guarantee that the available method returns the total
                    // size of the asset...  of course, this does mean that a single
                    // asset can't be more than 2 gigs.
                    int size = is.available();
                    // Read the entire asset into a local byte buffer.
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    // Convert the buffer into a string.
                    result = new String(buffer);

                    try {
                        new offlineJsonFileUtils(getBaseContext()).createJsonFileData(file_name[i], result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    // Should never happen!
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        recyclerDataList.clear();
        OffLineData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // dialog_status =false;

    }

    public void getUpdatedData() {
        new getJSON().execute(SourceURL);

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private class getJSON extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            String result = GET(urls[0]);
            try {
                /**Online JSON read*/
                JSONObject json = new JSONObject(result);
                articles = json.getJSONArray("article_list");
                online_jasonObjectLenth = json.getJSONArray("article_list").length();
                /**Offline Stored JSON read*/
                String jsonString_previous = new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData("json_string");
                JSONObject json_previous = new JSONObject(jsonString_previous);
                articles_previous = json_previous.getJSONArray("article_list");
                offline_jasonObjectLength = json_previous.getJSONArray("article_list").length();
                String online_ver_string = (String) json.get("version");
                String offline_ver_string = (String) json_previous.get("version");
                float online_ver = Float.parseFloat(online_ver_string);
                float offline_ver = Float.parseFloat(offline_ver_string);

                if (online_ver > offline_ver) {
                    update_status = true;

                    /**Clear Json Data From ArrayList*/

                    OnlineJsonData.clear();
                    OfflineJsonData.clear();
                    UpdateJsonData.clear();
                    DeleteJsonData.clear();

                    /**Online Json Data */
                    for (int i = 0; i < online_jasonObjectLenth; i++) {
                        String root = articles.getJSONObject(i).getString("root_path");
                        int  menu_ver = Integer.parseInt(articles.getJSONObject(i).getString("menu_version"));
                        String url = articles.getJSONObject(i).getString("url");

                        jsonDataList Data;
                        Data = new jsonDataList(root,menu_ver,url);
                        OnlineJsonData.add(Data);

                    }
                    /**Offline Json Data*/
                    for(int j =0 ; j<offline_jasonObjectLength;j++){

                        String root = articles.getJSONObject(j).getString("root_path");
                        int  menu_ver = Integer.parseInt(articles.getJSONObject(j).getString("menu_version"));
                        String url = articles.getJSONObject(j).getString("url");

                        jsonDataList Data2;
                        Data2 = new jsonDataList(root,menu_ver,url);
                        OfflineJsonData.add(Data2);
                    }
                    int totalProgress= 0;

                    for(int i =0 ;i< OnlineJsonData.size();i++)
                    {

                        int checkNew = 0;
                        jsonDataList OnlineData = OnlineJsonData.get(i);
                        String online_root =  OnlineData.getroot_path();
                        int onlineMenuVersion = OnlineData.getmenu_version();
                        for(int j = 0 ; j< OfflineJsonData.size() ; j++)
                        {
                            jsonDataList OfflineData = OfflineJsonData.get(j);
                            String offline_root =  OfflineData.getroot_path();
                            int offlineMenuVersion = OfflineData.getmenu_version();

                            if(offline_root.equals(online_root)){
                                if(onlineMenuVersion>offlineMenuVersion)
                                {
                                    UpdateJsonData.add(OnlineData);
                                    totalProgress++;
                                }
                                checkNew++;
                            }

                        }
                     if(checkNew==0)
                    {
                        UpdateJsonData.add(OnlineData);
                        totalProgress++;
                    }

                    }
                    for(int i =0 ;i< OfflineJsonData.size();i++)
                    {
                        int checkDelete = 0 ;
                        jsonDataList OfflineData = OfflineJsonData.get(i);
                        String offline_root =  OfflineData.getroot_path();
                        for(int j = 0 ; j< OnlineJsonData.size() ; j++)
                        {
                            jsonDataList OnlineData = OnlineJsonData.get(j);
                            String online_root =  OnlineData.getroot_path();
                            if(offline_root.equals(online_root)){

                                checkDelete++;
                            }

                        }if(checkDelete==0)
                    {
                        DeleteJsonData.add(OfflineData);
                    }
                    }
                    //End
                    progressMax = UpdateJsonData.size();
                    Log.d("Nazmul", String.valueOf(progressMax));
                } else {
                    update_status = false;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return result;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(final String result) {


            if(reload_status ==false)
            {
                if(update_status== false)
                {
                    reload_status = true;
                    progressSpiner.dismiss();
                    open_dialog();

                }else if(update_status==true)
                {
                    reload_status = true;
                    progressSpiner.dismiss();

                    download(progressMax);
                    jsonData = result;

                    new HttpAsyncTask().execute(jsonData);
                }
            }else if(reload_status ==true)
            {
                if(update_status) {

                    download(progressMax);
                    jsonData = result;
                    new HttpAsyncTask().execute(jsonData);
                }
            }

        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... urls) {
            String result = urls[0];
            try {
                for(int k = 0 , j =0 ; k< UpdateJsonData.size(); k++)
                {
                   jsonDataList UpdateData = UpdateJsonData.get(k);
                    String HtmlFileName = UpdateData.getroot_path();
                    String htmlPageUrl = UpdateData.getUrl();
                    htmlDocument = Jsoup.connect(htmlPageUrl).get();
                    htmlContentInStringFormat = htmlDocument.toString();

                    try {
                        new ReadWriteJsonFileUtils(getBaseContext()).createJsonFileData(HtmlFileName, htmlContentInStringFormat);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("HTMLsave","Error");
                    }

                    j++;
                    onProgressUpdate(j);
                    downloadedItem = j;
                    Log.d("DownloadItem ", String.valueOf(downloadedItem));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onProgressUpdate(Integer... values) {

            progress.setProgress(values[0]);
            if (values[0] + 1 == progressMax)
                progress.dismiss();

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(final String result) {
            TextView ShowVersion = (TextView) findViewById(R.id.versionName);
            ShowVersion.setText("Version : 3.0 Data Version : " + ver);
            Log.d("PostEx",String.valueOf(downloadedItem)+" "+ String.valueOf(progressMax));
            if (downloadedItem  == progressMax) {
                reload_status = true;
                try {
                    new ReadWriteJsonFileUtils(getBaseContext()).createJsonFileData(file_name, result);
                    Toast.makeText(getBaseContext(), "All  data Updated", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                recyclerDataList.clear();
                for (int i = 0; i < online_jasonObjectLenth; i++) {
                    RecyclerData recyclerData = null;
                    try {
                        recyclerData = new RecyclerData(articles.getJSONObject(i).getString("menu_name"), articles.getJSONObject(i).getString("last_update"), "", articles.getJSONObject(i).getString("root_path"), articles.getJSONObject(i).getString("type"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    recyclerDataList.add(recyclerData);
                }
            }
            mAdapter.notifyDataSetChanged();
        }

    }
    /**
     * Store Data into file
     */
    public class offlineJsonFileUtils {
        Context context;

        public offlineJsonFileUtils(Context context) {
            this.context = context;
        }

        public void createJsonFileData(String filename, String mJsonResponse) {
            try {
                File checkFile = new File(context.getApplicationInfo().dataDir + "/" + directoryName + "/");
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
                File f = new File(context.getApplicationInfo().dataDir + "/" + directoryName + "/" + filename);
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

        /**
         * delete All
         */
        public void deleteFile() {
            File f = new File(context.getApplicationInfo().dataDir + "/" + directoryName + "/");
            File[] files = f.listFiles();
            for (File fInDir : files) {
                fInDir.delete();
            }
        }

        public void deleteFile(String fileName) {
            File f = new File(context.getApplicationInfo().dataDir + "/" + directoryName + "/" + fileName);
            if (f.exists()) {
                f.delete();
            }
        }
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
                File checkFile = new File(context.getApplicationInfo().dataDir + "/" + directoryName + "/");
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
                File f = new File(context.getApplicationInfo().dataDir + "/" + directoryName + "/" + filename);
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
            File f = new File(context.getApplicationInfo().dataDir + "/" + directoryName + "/");
            File[] files = f.listFiles();
            for (File fInDir : files) {
                fInDir.delete();
            }
        }

        public void deleteFile(String fileName) {
            File f = new File(context.getApplicationInfo().dataDir + "/" + directoryName + "/" + fileName);
            if (f.exists()) {
                f.delete();
            }
        }
    }

    public void circularProgressBar() {
        progressSpiner = new ProgressDialog(MainActivity.this);
        progressSpiner.setMessage("Checking for update ...");
        progressSpiner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressSpiner.setCancelable(true);
        //progress.setMax(total);
        progressSpiner.setIndeterminate(true);
        // progress.setProgress(0);
        progressSpiner.show();

    }

    public void open_dialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Your have already updated data.");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        // if(dialog_status) {
        alertDialog.show();
        // }
    }

    public void download(int total) {
        progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Downloading Updated Data ...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(true);
        progress.setMax(total);
        //progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progress.show();

    }



}