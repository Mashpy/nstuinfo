package com.nstuinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.nstuinfo.mViews.FontAppearance;

import at.blogc.android.views.ExpandableTextView;

public class IntroActivity extends Activity {

    ViewFlipper viewFlipper;
    ExpandableTextView expTextView1, expTextView2;
    Button readMoreBtn1, readMoreBtn2;
    Button enterBtn, enterImgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            expTextView1.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            expTextView2.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.2f);
            expTextView1.setLayoutParams(params);
            expTextView2.setLayoutParams(params);
        }

        expTextView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandingAndCollapsingTV(expTextView1, readMoreBtn1);
            }
        });

        expTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandingAndCollapsingTV(expTextView2, readMoreBtn2);
            }
        });

        readMoreBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandingAndCollapsingTV(expTextView1, readMoreBtn1);
            }
        });

        readMoreBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandingAndCollapsingTV(expTextView2, readMoreBtn2);
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

        readMoreBtn1 = findViewById(R.id.btnReadMore1);
        readMoreBtn2 = findViewById(R.id.btnReadMore2);

        expTextView1.setInterpolator(new OvershootInterpolator());
        expTextView2.setInterpolator(new OvershootInterpolator());

        enterBtn = findViewById(R.id.enterBtn);
        enterImgBtn = findViewById(R.id.enterBtnImg);
    }

    private void finishAndStart() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    private void expandingAndCollapsingTV(ExpandableTextView eptv, Button button) {
        if (eptv.isExpanded()) {
            eptv.collapse();
            button.setText("Read more");
        } else {
            eptv.expand();
            button.setText("Collapse text");
        }
        //eptv.toggle();
    }

    @Override
    public void onBackPressed() {
        finishAndStart();
    }
}
