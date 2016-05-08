package com.mashpy.nstuinfo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageViewUse extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String imagename = "safe_image";
        Bitmap b;
        ImageView imageView = (ImageView) findViewById(R.id.my_image);
        String imgurl = "http://nazmul56.github.io/safe_image.jpg";

        if (ImageStorage.checkifImageExists(imagename, getBaseContext())) {
            File file = ImageStorage.getImage("/" + imagename + ".jpg", getBaseContext());
            String path = file.getAbsolutePath();
            if (path != null) {
                b = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(b);
            }

        } else {
            new GetImages(imgurl, imageView, imagename).execute();
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

    public static class ImageStorage {

        public static String saveToSdCard(Bitmap bitmap, String filename, Context context) {

            String stored = null;

            //File sdcard = Environment.getExternalStorageDirectory() ;
            File folder = new File(context.getApplicationInfo().dataDir + "/nazmul/");

            File sdcard = Environment.getDataDirectory();
            //return checkFile.toString() ;
            //\  File f = new File("data/data/"+ "/databases");
            // File folder = new File(sdcard.getAbsoluteFile(), "nazmul");//the dot makes this directory hidden to the user
            folder.mkdir();
            File file = new File(folder.getAbsoluteFile(), filename + ".jpg");
            if (file.exists())
                return stored;

            try {

                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                stored = "success";


            } catch (Exception e) {
                e.printStackTrace();
            }
            return stored;
        }

        public static File getImage(String imagename, Context context) {

            File mediaImage = null;
            try {


                String root = context.getApplicationInfo().dataDir.toString();
                File myDir = new File(root);
                if (!myDir.exists())
                    return null;

                mediaImage = new File(myDir.getPath() + "/nazmul/" + imagename);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return mediaImage;
        }

        public static boolean checkifImageExists(String imagename, Context context) {
            Bitmap b = null;
            File file = ImageStorage.getImage("/" + imagename + ".jpg", context);
            String path = file.getAbsolutePath();

            if (path != null)
                b = BitmapFactory.decodeFile(path);

            if (b == null || b.equals("")) {
                return false;
            }
            return true;
        }
    }

    private class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl, imagename_;
        private ImageView view;
        private Bitmap bitmap;
        private FileOutputStream fos;

        private GetImages(String requestUrl, ImageView view, String _imagename_) {
            this.requestUrl = requestUrl;
            this.view = view;
            this.imagename_ = _imagename_;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (!ImageStorage.checkifImageExists(imagename_, getBaseContext())) {
                view.setImageBitmap(bitmap);
                String status = ImageStorage.saveToSdCard(bitmap, imagename_, getBaseContext());
                Toast.makeText(getBaseContext(), "Status : " + status, Toast.LENGTH_LONG).show();
            }
        }
    }

}
