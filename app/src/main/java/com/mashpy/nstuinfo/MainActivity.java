package com.mashpy.nstuinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

    private List<RecyclerData> recyclerDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerDataAdapter mAdapter;
    private Document htmlDocument;
    private String htmlContentInStringFormat;
    private String htmlfile_name;
    public  String expire_date;

    private ProgressDialog progress;

    int jumpTime = 0;
    int json_length = 0;
    int increment =0;
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                //Intent i = new Intent(getApplicationContext(), ImageViewUse.class);
                //startActivity(i);
                prepareMovieData();
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
                details.putExtra("exp",expire_date);
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

        if (id == R.id.action_settings) {
            return true;
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

        new HttpAsyncTask().execute("https://raw.githubusercontent.com/Mashpy/nstuinfo/develop/version.json");

    }

    public void OffLineData() {
        String result;
        String file_name = "json_string";
        if (new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name) == null) {
            try {
                InputStream is = getAssets().open("json_string");

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

            String str = "";

            String jsonString = new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name);

            JSONObject json = new JSONObject(jsonString);

            JSONArray articles = json.getJSONArray("article_list");

            int jasonObjecLenth = json.getJSONArray("article_list").length();
            for (int i = 0; i < jasonObjecLenth; i++) {

                RecyclerData recyclerData = new RecyclerData(articles.getJSONObject(i).getString("menu_name"), articles.getJSONObject(i).getString("last_update"), articles.getJSONObject(i).getString("menu_version"), articles.getJSONObject(i).getString("root_path"));
                recyclerDataList.add(recyclerData);

            }
            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }


    public void offlineHtml(){

        String result = "";
        String file_name[] = {"introduction", "regentboard","academic_council","committees","register_office","central_library","dept_teacher","cr","academic_calender","academic_officiary", "administrative","student_activities","transport_section","hall_office","emergency_contacts","message_from_developer"};
        for(int i = 0; i<16 ;i++) {

            if (new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name[i]) == null) {
                try {
                    InputStream is = getAssets().open(file_name[i]);

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

    public void download(int total){
        progress=new ProgressDialog(MainActivity.this);
        progress.setMessage("Downloading Updated Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(true);
        progress.setMax(total);
        // progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progress.show();
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();

        recyclerDataList.clear();
        OffLineData();

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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(final String result) {
            try {
                /**Online JSON read*/
                JSONObject json = new JSONObject(result);
                final JSONArray articles = json.getJSONArray("article_list");
                final int online_jasonObjectLenth = json.getJSONArray("article_list").length();
                /**Offline Stored JSON read*/
                String jsonString_previous = new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData("json_string");
                JSONObject json_previous = new JSONObject(jsonString_previous);
                final JSONArray articles_previous = json_previous.getJSONArray("article_list");
                /** JSON Version*/
                String online_ver_string = (String) json.get("version");
                String offline_ver_string = (String) json_previous.get("version");

                float online_ver = Float.parseFloat(online_ver_string);
                float offline_ver = Float.parseFloat(offline_ver_string);

                if (online_ver > offline_ver) {

                   download(online_jasonObjectLenth);

                    final int totalProgressTime = online_jasonObjectLenth;
                    final Thread t = new Thread() {
                        @Override
                        public void run() {
                            jumpTime = 0;

                            while(jumpTime < totalProgressTime) {
                                try {

                                    //  jumpTime += 5;
                                    for (int i = 0; i < online_jasonObjectLenth; i++) {
                                        increment =i;
                                        sleep(2);
                                        String html_file_name = articles.getJSONObject(i).getString("root_path");
                                        String htmlPageUrl = articles.getJSONObject(i).getString("url");

                                        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                                        jsoupAsyncTask.execute(htmlPageUrl, html_file_name);
                                        progress.setProgress(jumpTime);
                                    }

                                }
                                catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            //progress.dismiss();
                        }
                    };
                    t.start();

                    String file_name = "json_string";
                    try {
                        new ReadWriteJsonFileUtils(getBaseContext()).createJsonFileData(file_name, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getBaseContext(), "Update All  data", Toast.LENGTH_LONG).show();

                } else {

                    int  menu_update_number =0;
                    for (int i = 0; i < online_jasonObjectLenth; i++) {

                        if (Integer.parseInt(articles.getJSONObject(i).getString("menu_version")) > Integer.parseInt(articles_previous.getJSONObject(i).getString("menu_version"))) {
                            menu_update_number++;
                        }
                    }
                    if(menu_update_number>0) {
                        download(menu_update_number);
                    }
                    final int totalProgressTime = menu_update_number;
                    final Thread t = new Thread() {
                        @Override
                        public void run() {
                            jumpTime = 0;

                            while(jumpTime < totalProgressTime) {
                                try {

                    for (int i = 0; i < online_jasonObjectLenth; i++) {
                        sleep(2);
                        String html_file_name = articles.getJSONObject(i).getString("root_path");
                        String htmlPageUrl = articles.getJSONObject(i).getString("url");
                        if (Integer.parseInt(articles.getJSONObject(i).getString("menu_version")) > Integer.parseInt(articles_previous.getJSONObject(i).getString("menu_version"))) {
                            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                            jsoupAsyncTask.execute(htmlPageUrl, html_file_name);
                            progress.setProgress(jumpTime);
                        }
                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            //progress.dismiss();
                        }
                    };
                    t.start();

                    String file_name = "json_string";
                    try {
                        new ReadWriteJsonFileUtils(getBaseContext()).createJsonFileData(file_name, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                recyclerDataList.clear();

                for (int i = 0; i < online_jasonObjectLenth; i++) {

                    RecyclerData recyclerData = new RecyclerData(articles.getJSONObject(i).getString("menu_name"), articles.getJSONObject(i).getString("last_update"), articles.getJSONObject(i).getString("menu_version"), articles.getJSONObject(i).getString("root_path"));
                    recyclerDataList.add(recyclerData);

                }
                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class JsoupAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... url) {
            try {
                htmlDocument = Jsoup.connect(url[0]).get();
                htmlContentInStringFormat = htmlDocument.toString();
                htmlfile_name = url[1];
               // if(htmlContentInStringFormat != "")
               // {
                jumpTime += 1;
               // }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                new ReadWriteJsonFileUtils(getBaseContext()).createJsonFileData(htmlfile_name, htmlContentInStringFormat);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class HttpAsyncTask_Update_data extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

        }
    }

    /**
     * Store Data into file
     */
    public class offlineJsonFileUtils {
        Activity activity;
        Context context;

        public offlineJsonFileUtils(Context context) {

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