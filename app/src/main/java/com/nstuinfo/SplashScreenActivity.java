package com.nstuinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nstuinfo.mOtherUtils.AnimationUtils;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashScreenActivity extends Activity {

    private static final int SPLASH_SCREEN_TIME = 2500;
    private ImageView topImg, bottomImg;
    private RelativeLayout middleRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        topImg = findViewById(R.id.topIM);
        bottomImg = findViewById(R.id.bottomIM1);
        middleRL = findViewById(R.id.middleShapeRL);

        AnimationUtils.shake(this, topImg);
        AnimationUtils.shake(this, bottomImg);
        AnimationUtils.leftToRightAnimation(middleRL, 1000);

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
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
