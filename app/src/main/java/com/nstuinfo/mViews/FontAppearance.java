package com.nstuinfo.mViews;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

import com.nstuinfo.mOtherUtils.Preferences;

import java.lang.reflect.Field;

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

    public static void setTextFontFace(Context context, TextView textView, String fontName) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "roboto_regular");
        textView.setTypeface(tf);
    }

    public static void setTextFontFace(Context context, Button button, String fontName) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontName);
        button.setTypeface(tf);
    }

    public static void replaceDefaultFont (Context context) {

        Typeface customFontTypeFace = Typeface.createFromAsset(context.getAssets(), "roboto_regular.ttf");
        replaceFont("DEFAULT", customFontTypeFace);

    }

    private static void replaceFont(String nameOfFontBeingReplaced, Typeface customFontTypeFace) {
        try {
            Field myfield = Typeface.class.getDeclaredField(nameOfFontBeingReplaced);
            myfield.setAccessible(true);
            myfield.set(null, customFontTypeFace);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
