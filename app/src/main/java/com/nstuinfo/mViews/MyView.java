package com.nstuinfo.mViews;

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
import android.text.Layout;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nstuinfo.R;
import com.nstuinfo.mOtherUtils.AnimationUtils;
import com.nstuinfo.mOtherUtils.Preferences;

import at.blogc.android.views.ExpandableTextView;

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

    // UNUSED METHOD
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

        String[] splitArray = content.split("<br />|<br/>|<br>");

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
                tv.setPadding(tv.getPaddingLeft(), 3, tv.getPaddingRight()+10, 3);
                icon.setImageResource(android.R.drawable.sym_action_call);
                icon.setVisibility(View.VISIBLE);
            }

            if (s.contains("Email") || s.contains("E-mail") || s.contains("Mail") ||
                    s.contains("email:") || s.contains("e-mail:") || s.contains("mail:")) {
                tv.setPadding(tv.getPaddingLeft(), 3, tv.getPaddingRight()+10, 3);
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

    @SuppressLint("InflateParams")
    public static void setImageContentView(Context context, String content, LinearLayout linearLayout, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.image_content_view,null);
        }

        assert layout != null;
        ImageView imageView = layout.findViewById(R.id.imageContentImgView);
        TextView tv = layout.findViewById(R.id.imageContentTV);

        String[] s = content.split(",");
        String imgName = "", imgFooter = "";
        if (s.length == 1) {
            imgName = s[0];
        } else {
            imgName = s[0];
            for (int i=1; i<s.length; i++) {
                imgFooter += s[i];
            }
        }

        int imageId = context.getResources()
                .getIdentifier(imgName.trim().toLowerCase(), "drawable", context.getPackageName());

        if (imageId > 0) {
            imageView.setImageResource(imageId);
        } else {
            Toast.makeText(context, "Image doesn't found!!", Toast.LENGTH_SHORT).show();
            imageView.setVisibility(View.GONE);
        }

        if (imgFooter.trim().equalsIgnoreCase("")) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tv.setText(Html.fromHtml(imgFooter.trim(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                tv.setText(Html.fromHtml(imgFooter.trim()));
            }
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
    public static void setExpandableContentView (Context context, String content, LinearLayout linearLayout, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.expandable_content_view,null);
        }

        assert layout != null;
        final ExpandableTextView etv = layout.findViewById(R.id.expandableTextView);
        CardView cardView = layout.findViewById(R.id.content_item_view_card);

        if (Preferences.isDarkTheme(context)) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.dark_color_secondary));
            etv.setTextColor(Color.WHITE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            etv.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            etv.setText(Html.fromHtml(content));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            etv.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.2f);
            etv.setLayoutParams(params);
        }

        etv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etv.toggle();
            }
        });

        FontAppearance.setSecondaryTextSize(context, etv);

        Linkify.addLinks(etv, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        etv.setLinksClickable(true);

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
