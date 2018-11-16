package com.mashpy.nstuinfo.mOtherUtils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;

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

}
