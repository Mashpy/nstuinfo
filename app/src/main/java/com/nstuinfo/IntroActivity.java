package com.nstuinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.nstuinfo.mViews.FontAppearance;

import at.blogc.android.views.ExpandableTextView;

public class IntroActivity extends Activity {

    ViewFlipper viewFlipper;
    ExpandableTextView expTextView1, expTextView2;
    Button enterBtn, enterImgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initViews();

        expTextView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //expTextView1.toggle();
                if (expTextView1.isExpanded()) {
                    expTextView1.collapse();
                } else {
                    expTextView1.expand();
                }
            }
        });

        expTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //expTextView2.toggle();
                if (expTextView2.isExpanded()) {
                    expTextView2.collapse();
                } else {
                    expTextView2.expand();
                }
            }
        });

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndStart();
            }
        });

        enterImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndStart();
            }
        });

        FontAppearance.replaceDefaultFont(this);

    }

    private void initViews() {
        viewFlipper = findViewById(R.id.viewFlipper);
        viewFlipper.setInAnimation(this, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(this, android.R.anim.fade_out);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(2000);

        expTextView1 = findViewById(R.id.expandableTextView1);
        expTextView2 = findViewById(R.id.expandableTextView2);

        expTextView1.setInterpolator(new OvershootInterpolator());
        expTextView2.setInterpolator(new OvershootInterpolator());

        enterBtn = findViewById(R.id.enterBtn);
        enterImgBtn = findViewById(R.id.enterBtnImg);
    }

    private void finishAndStart() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        finishAndStart();
    }
}
