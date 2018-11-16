package com.mashpy.nstuinfo.mViews;

import android.content.Context;
import android.widget.TextView;

import com.mashpy.nstuinfo.mOtherUtils.Preferences;

/**
 * Created by whoami on 10/30/2018.
 */

public class FontAppearance {

    public static void setPrimaryTextSize(Context context, TextView textView) {
        if (Preferences.getFontAppearance(context).equals(Preferences.LARGE_FONT)) {
            textView.setTextSize(20);
        } else if (Preferences.getFontAppearance(context).equals(Preferences.MEDIUM_FONT)) {
            textView.setTextSize(18);
        } else {
            textView.setTextSize(16);
        }
    }

    public static void setSecondaryTextSize(Context context, TextView textView) {
        if (Preferences.getFontAppearance(context).equals(Preferences.LARGE_FONT)) {
            textView.setTextSize(18);
        } else if (Preferences.getFontAppearance(context).equals(Preferences.MEDIUM_FONT)) {
            textView.setTextSize(16);
        } else {
            textView.setTextSize(14);
        }
    }
}
