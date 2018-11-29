package com.nstuinfo.mJsonUtils;

import android.content.Context;
import android.widget.Toast;

import com.nstuinfo.mViews.MyView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by whoami on 11/8/2018.
 */

public class ExtractInitialJson {

    private Context context;
    private String text;

    public ExtractInitialJson (Context context, String text) {
        this.context  = context;
        this.text = text;
    }

    public Double getDataVersionFromInitialJson() {
        double dataVersion = 0.0;
        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("data_version")) {
                    try {
                        dataVersion = Double.valueOf(object.getString("data_version").trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Exception arise while converting string to double!!", Toast.LENGTH_LONG).show();
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataVersion;
    }

    public String getDataUrl() {
        String dataUrl = "";
        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("data_url")) {
                    dataUrl = object.getString("data_url").trim();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataUrl;
    }

    public void getPopupNotificationDialog() {
        String popUpTitle = "", popUpMsg = "", popUpPosBtn="", popUpNegBtn="", popUpPosUrlLink="";
        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("popup_notification_title")) {
                    popUpTitle = object.getString("popup_notification_title").trim();
                }

                if (object.has("popup_notification_message")) {
                    popUpMsg = object.getString("popup_notification_message").trim();
                }

                if (object.has("popup_notification_positive_btn")) {
                    popUpPosBtn = object.getString("popup_notification_positive_btn").trim();
                }

                if (object.has("popup_notification_positive_url_link")) {
                    popUpPosUrlLink = object.getString("popup_notification_positive_url_link").trim();
                }

                if (object.has("popup_notification_negative_btn")) {
                    popUpNegBtn = object.getString("popup_notification_negative_btn").trim();
                }

                MyView.setPopupDialog(context, popUpTitle, popUpMsg, popUpPosBtn, popUpNegBtn, popUpPosUrlLink);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
