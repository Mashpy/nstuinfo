package com.nstuinfo.mOtherUtils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.nstuinfo.R;

/**
 * Created by whoami on 10/16/2018.
 */

public class AnimationUtils {

    public static void animateRecyclerView(View view , boolean goesDown){
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", goesDown ? 200 : -200, 0);
        animatorTranslateY.setDuration(1000);

        animatorSet.playTogether(animatorTranslateY);
        animatorSet.start();

    }

    public static void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    public static void rightToLeftAnimation (View view, int duration) {
        TranslateAnimation animation = new TranslateAnimation(1000.0f, 0.0f, 0.0f, 0.0f);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public static void leftToRightAnimation (View view, int duration) {
        TranslateAnimation animation = new TranslateAnimation(-1000.0f, 0.0f, 0.0f, 0.0f);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public static void shake(Context context, final View view) {

        final Animation shake1 = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.shake_1);
        final Animation shake2 = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.shake_2);

        view.startAnimation(shake1);

        shake1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(shake2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        shake2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(shake1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
