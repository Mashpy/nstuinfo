package com.nstuinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nstuinfo.mConnectivity.CheckUpdateAvailability;
import com.nstuinfo.mConnectivity.UrlResponse;
import com.nstuinfo.mJsonUtils.Constants;
import com.nstuinfo.mOtherUtils.AnimationUtils;
import com.nstuinfo.mOtherUtils.ExtraUtils;

import java.util.Arrays;

import androidx.annotation.NonNull;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.nstuinfo.mJsonUtils.Constants.INITIALIZE_CONSTANTS;

public class SplashScreenActivity extends Activity {

    private static final int SPLASH_SCREEN_TIME = 3000;
    private ImageView topImg, bottomImg;
    private RelativeLayout middleRL;
    private TextView tvVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        INITIALIZE_CONSTANTS();

        topImg = findViewById(R.id.topIM);
        bottomImg = findViewById(R.id.bottomIM1);
        middleRL = findViewById(R.id.middleShapeRL);
        tvVersionName = findViewById(R.id.tv_version_name);

        tvVersionName.setText(BuildConfig.VERSION_NAME);

        AnimationUtils.setFadeAnimation(tvVersionName);
        AnimationUtils.shake(this, topImg);
        AnimationUtils.shake(this, bottomImg);
        AnimationUtils.leftToRightAnimation(middleRL, 1000);

        if (ExtraUtils.isInternetOn(this)) {
            checkUpdateAvailability(this);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        }, SPLASH_SCREEN_TIME);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    /**
     * Check whether update is available or not
     * @param activity - current context
     */
    private void checkUpdateAvailability(final Activity activity) {

        final String currentVersion = BuildConfig.VERSION_NAME;
        final String APP_URL = "https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en";

        new CheckUpdateAvailability(APP_URL, new UrlResponse() {

            @Override
            public void onReceived(String onlineVersion) {
                //String text = "Current Version = " + currentVersion + "\nOnline Version = " + onlineVersion;
                //Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
                //Log.d("VERSION", text);

                try {

                    if (Float.valueOf(onlineVersion) > Float.valueOf(currentVersion)) {
                        Constants.UPDATE_AVAILABLE = true;
                    } else {
                        Constants.UPDATE_AVAILABLE = false;
                    }

                } catch (Exception e) {

                    try {

                        String[] online = onlineVersion.split("\\.");
                        String[] offline = currentVersion.split("\\.");

                        Constants.UPDATE_AVAILABLE = false;

                        for (int i = 0; i < online.length; i++) {
                            if (Integer.parseInt(online[i].trim()) > Integer.parseInt(offline[i])) {
                                Constants.UPDATE_AVAILABLE = true;
                                break;
                            }
                        }

                    } catch (Exception ignored) {}

                }


            }
        }).execute();

    }

}
