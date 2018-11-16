package com.mashpy.nstuinfo.mOtherUtils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by whoami on 10/30/2018.
 */

public class Preferences {

    public static final String THEME = "Theme";
    public static final String FONT = "Font";
    public static final String SMALL_FONT = "Small font";
    public static final String MEDIUM_FONT = "Medium font";
    public static final String LARGE_FONT = "Large font";
    public static final String VIEW = "View";

    public static void setDarkTheme(Context context, boolean b) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(THEME, b)
                .apply();
    }

    public static boolean isDarkTheme(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(THEME, false);
    }

    public static void setFontAppearance(Context context, String fontAppearance) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(FONT, fontAppearance)
                .apply();
    }

    public static String getFontAppearance(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(FONT, SMALL_FONT);
    }

    public static void setGridView(Context context, boolean b) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(VIEW, b)
                .apply();
    }

    public static boolean isGridView(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(VIEW, true);
    }

}
