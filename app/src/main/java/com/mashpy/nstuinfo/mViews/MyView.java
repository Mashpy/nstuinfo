package com.mashpy.nstuinfo.mViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mashpy.nstuinfo.R;
import com.mashpy.nstuinfo.mOtherUtils.AnimationUtils;
import com.mashpy.nstuinfo.mOtherUtils.Preferences;

/**
 * Created by whoami on 10/26/2018.
 */

public class MyView {

    @SuppressLint("InflateParams")
    public static void setTitleView(Context context, String text, LinearLayout linearLayout, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.title_view,null);
        }

        assert layout != null;
        TextView tv = layout.findViewById(R.id.titleTV);
        CardView cardView = layout.findViewById(R.id.title_item_view_card);

        if (Preferences.isDarkTheme(context)) {
            cardView.setCardBackgroundColor(Color.BLACK);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv.setText(Html.fromHtml(text));
        }

        FontAppearance.setPrimaryTextSize(context, tv);

        Linkify.addLinks(tv, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        tv.setLinksClickable(true);

        AnimationUtils.rightToLeftAnimation(layout, (700+(position*100)));

        linearLayout.addView(layout);
    }

    @SuppressLint("InflateParams")
    public static void setHintView(Context context, String content, LinearLayout linearLayout, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.hint_view,null);
        }

        assert layout != null;
        TextView tv = layout.findViewById(R.id.hintTV);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv.setText(Html.fromHtml(content));
        }

        if (Preferences.isDarkTheme(context)) {
            tv.setTextColor(Color.WHITE);
        }

        FontAppearance.setSecondaryTextSize(context, tv);

        Linkify.addLinks(tv, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        tv.setLinksClickable(true);

        AnimationUtils.rightToLeftAnimation(layout, (700+(position*100)));

        linearLayout.addView(layout);
    }

    @SuppressLint("InflateParams")
    public static void setContentView (Context context, String content, LinearLayout linearLayout, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.content_view,null);
        }

        assert layout != null;
        TextView tv = layout.findViewById(R.id.contentTV);
        CardView cardView = layout.findViewById(R.id.content_item_view_card);

        if (Preferences.isDarkTheme(context)) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.dark_color_secondary));
            tv.setTextColor(Color.WHITE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv.setText(Html.fromHtml(content));
        }

        FontAppearance.setSecondaryTextSize(context, tv);

        Linkify.addLinks(tv, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        tv.setLinksClickable(true);

        ImageView callImg = layout.findViewById(R.id.phoneIMGV);
        ImageView mailImg = layout.findViewById(R.id.mailIMGV);
        callImg.setVisibility(View.GONE);
        mailImg.setVisibility(View.GONE);

        if ( content.contains("Phone") || content.contains("Telephone") || content.contains("Mobile") ||
                content.contains("phone:") || content.contains("telephone:") || content.contains("mobile:") ) {

            callImg.setVisibility(View.VISIBLE);
        }

        if (content.contains("Email") || content.contains("E-mail") || content.contains("Mail") ||
                content.contains("email:") || content.contains("e-mail:") || content.contains("mail:")) {

            mailImg.setVisibility(View.VISIBLE);
        }

        AnimationUtils.rightToLeftAnimation(layout, (700+(position*100)));

        linearLayout.addView(layout);
    }

    @SuppressLint("InflateParams")
    public static void setContentView2 (Context context, String content, LinearLayout linearLayout, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.content_view2,null);
        }

        assert layout != null;
        LinearLayout contentLL = layout.findViewById(R.id.contentViewLL);
        CardView cardView = layout.findViewById(R.id.content_item_view_card);

        if (Preferences.isDarkTheme(context)) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.dark_color_secondary));
        }

        String[] splitArray = content.split("<br />");

        for (String s : splitArray) {

            LayoutInflater inflater2 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout2 = null;

            if (inflater2 != null) {
                layout2 = inflater.inflate(R.layout.content_view_tvs, null);
            }

            assert layout2 != null;
            TextView tv = layout2.findViewById(R.id.contentTV);
            ImageView icon = layout2.findViewById(R.id.iconImageView);

            icon.setVisibility(View.GONE);

            if (Preferences.isDarkTheme(context)) {
                tv.setTextColor(Color.WHITE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tv.setText(Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY));
            } else {
                tv.setText(Html.fromHtml(s));
            }

            if ( s.contains("Phone") || s.contains("Telephone") || s.contains("Mobile") ||
                    s.contains("phone:") || s.contains("telephone:") || s.contains("mobile:") ) {

                icon.setImageResource(android.R.drawable.sym_action_call);
                icon.setVisibility(View.VISIBLE);
            }

            if (s.contains("Email") || s.contains("E-mail") || s.contains("Mail") ||
                    s.contains("email:") || s.contains("e-mail:") || s.contains("mail:")) {

                icon.setImageResource(R.drawable.ic_action_mail);
                icon.setVisibility(View.VISIBLE);
            }

            FontAppearance.setSecondaryTextSize(context, tv);

            Linkify.addLinks(tv, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
            tv.setLinksClickable(true);

            contentLL.addView(layout2);
        }


        AnimationUtils.rightToLeftAnimation(layout, (700+(position*100)));

        linearLayout.addView(layout);
    }

    public static void setPopupDialog(final Context context, String title, String message, String posBtn, String negBtn, final String posBtnUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (title.trim().equalsIgnoreCase("")) {
            builder.setTitle(Html.fromHtml("<font color='#0D47A1'>Notice!!</font>"));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#0D47A1'>"+title+"</font>"));
        }

        if (!message.trim().equalsIgnoreCase("")) {
            builder.setMessage(Html.fromHtml("<font color='#000000'>"+message+"</font>"));
        }

        if (posBtn.trim().equalsIgnoreCase("")) {
            builder.setPositiveButton(Html.fromHtml("OK"),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        } else {
            builder.setPositiveButton(Html.fromHtml(posBtn),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (posBtnUrl.trim().equalsIgnoreCase("")) {
                        dialog.cancel();
                    } else {
                        Uri uri = Uri.parse(posBtnUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                }
            });
        }

        if (!negBtn.trim().equalsIgnoreCase("")) {
            builder.setNegativeButton(Html.fromHtml(negBtn),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        AlertDialog alert = builder.create();

        if ( !message.equalsIgnoreCase(PreferenceManager.getDefaultSharedPreferences(context).getString("POPUP_MSG", "Default")) &&
                !message.equalsIgnoreCase("") ) {
            alert.show();
        }

        if (!message.equalsIgnoreCase("")) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("POPUP_MSG", message).apply();
        }

    }

}
