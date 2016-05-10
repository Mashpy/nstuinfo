package com.mashpy.nstuinfo;

import android.app.Activity;
import android.content.Context;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), ImageViewUse.class);

                startActivity(i);
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
                Toast.makeText(getApplicationContext(), recyclerData.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                Intent details = new Intent(MainActivity.this, DetailsActivity.class);
                details.putExtra("url", recyclerData.getUrl());
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

    private void prepareMovieData() {

        String result;
        String file_name = "update";
        if (new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name) == null) {
            try {
                InputStream is = getAssets().open("update");

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
        new HttpAsyncTask_ChackUpadte_data().execute("http://nazmul56.github.io/update.json");
        //  new HttpAsyncTask().execute("http://nazmul56.github.io/nget.json");

    }

    public void OffLineData() {

        Toast.makeText(getBaseContext(), "OffLine Data!", Toast.LENGTH_LONG).show();

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

        try {

            String str = "";

            String jsonString = new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name);

            JSONObject json = new JSONObject(jsonString);

            JSONArray articles = json.getJSONArray("articleList");

            int jasonObjecLenth = json.getJSONArray("articleList").length();
            for (int i = 0; i < jasonObjecLenth; i++) {

                RecyclerData recyclerData = new RecyclerData(articles.getJSONObject(i).getString("title"), articles.getJSONObject(i).getString("categories"), "", articles.getJSONObject(i).getString("url"));
                recyclerDataList.add(recyclerData);

            }
            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try {
                JSONObject json = new JSONObject(result);

                String str = "";

                String file_name = "json_string";

                try {
                    new ReadWriteJsonFileUtils(getBaseContext()).createJsonFileData(file_name, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                JSONArray articles = json.getJSONArray("articleList");
                str += "articles length = " + json.getJSONArray("articleList").length();//This section find the articleList
                str += "\n--------\n";
                str += "names: " + articles.getJSONObject(0).names();
                str += "\n--------\n";
                str += "url: " + articles.getJSONObject(1).getString("url");
                recyclerDataList.clear();

                int jasonObjecLenth = json.getJSONArray("articleList").length();
                for (int i = 0; i < jasonObjecLenth; i++) {

                    RecyclerData recyclerData = new RecyclerData(articles.getJSONObject(i).getString("title"), articles.getJSONObject(i).getString("categories"), "", articles.getJSONObject(i).getString("url"));
                    recyclerDataList.add(recyclerData);

                }
                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTask_ChackUpadte_data extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //  Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try {
                //JSON object from online
                JSONObject json = new JSONObject(result);

                String str = "";
                JSONArray articles = json.getJSONArray("update");
                String file_name = "update";

                //JSON Object From package folde
                String jsonString = new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name);
                JSONObject json_previous = new JSONObject(jsonString);
                JSONArray articles_previous = json_previous.getJSONArray("update");

                Toast.makeText(getBaseContext(), articles.getJSONObject(0).getString("update_ver"), Toast.LENGTH_LONG).show();

                if (Integer.parseInt(articles.getJSONObject(0).getString("update_ver")) > Integer.parseInt(articles_previous.getJSONObject(0).getString("update_ver"))) {

                    try {
                        new ReadWriteJsonFileUtils(getBaseContext()).createJsonFileData(file_name, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new HttpAsyncTask().execute("http://nazmul56.github.io/nget.json");


                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
