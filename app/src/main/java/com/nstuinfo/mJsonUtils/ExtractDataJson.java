package com.nstuinfo.mJsonUtils;

import android.content.Context;
import android.widget.LinearLayout;

import com.nstuinfo.mViews.MyView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by whoami on 10/24/2018.
 */

public class ExtractDataJson {

    private Context context;
    private String text;
    private LinearLayout linearLayout;

    public ExtractDataJson(Context context, String text) {
        this.context  = context;
        this.text = text;
    }

    public ExtractDataJson(Context context, String text, LinearLayout linearLayout) {
        this.context = context;
        this.text = text;
        this.linearLayout = linearLayout;
    }

    public Double getDataVersionFromDataJson() {
        double version = 0.0;

        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("data_version")) {
                    try {
                        version = Double.valueOf(object.getString("data_version").trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return version;
    }

    public String getVersionCheckURL() {
        String url = null;

        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("version_check_url")) {
                    try {
                        url = object.getString("version_check_url").trim();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }

    public List<String> getMainItemsList() {
        List<String> list = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("data_version")) {
                    String data_version = object.getString("data_version");
                }

                if (object.has("data")) {

                    JSONArray dataArray = object.getJSONArray("data");

                    for (int j=0; j<dataArray.length(); j++) {

                        JSONObject dataObject = (JSONObject) dataArray.get(j);

                        if (dataObject.has("item")) {
                            String item = dataObject.getString("item").trim();
                            list.add(item);
                        }
                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void getView(String itemTag) {

        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("data")) {

                    JSONArray dataArray = object.getJSONArray("data");

                    for (int j=0; j<dataArray.length(); j++) {

                        JSONObject dataObject = (JSONObject) dataArray.get(j);

                        if (dataObject.has("item")) {
                            String item = dataObject.getString("item").trim();

                            if (item.equalsIgnoreCase(itemTag)) {

                                if (dataObject.has("details")) {

                                    JSONArray detailsArray = dataObject.getJSONArray("details");

                                    for (int k=0; k<detailsArray.length(); k++) {

                                        JSONObject detailsObject = (JSONObject) detailsArray.get(k);

                                        if (detailsObject.has("title")) {
                                            String title = detailsObject.getString("title").trim();
                                            if (!title.equalsIgnoreCase("")) {
                                                if (detailsObject.has("contents")) {
                                                    //itemList.add(title);
                                                } else {
                                                    MyView.setTitleView(context, title, linearLayout, k);
                                                }
                                            }
                                        }

                                        if (detailsObject.has("hint")) {
                                            String hint = detailsObject.getString("hint").trim();
                                            if (!hint.equalsIgnoreCase("")) {
                                                MyView.setHintView(context, hint, linearLayout, k);
                                            }
                                        }

                                        if (detailsObject.has("content")) {
                                            String content = detailsObject.getString("content").trim();
                                            if (!content.equalsIgnoreCase("")) {
                                                MyView.setContentView2(context, content, linearLayout, k);
                                            }
                                        }

                                        if (detailsObject.has("image_content")) {
                                            String image_content = detailsObject.getString("image_content").trim();
                                            // image_content = ("imageName, imageFooter")
                                            if (!image_content.equalsIgnoreCase("")) {
                                                MyView.setImageContentView(context, image_content, linearLayout, k);
                                            }
                                        }

                                    }

                                }
                            }
                        }

                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPopUpView(String titleTag1, String titleTag2) {
        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("data")) {

                    JSONArray dataArray = object.getJSONArray("data");

                    for (int j=0; j<dataArray.length(); j++) {

                        JSONObject dataObject = (JSONObject) dataArray.get(j);

                        if (dataObject.has("item")) {
                            String item = dataObject.getString("item").trim();

                            if (item.equalsIgnoreCase(titleTag1)) {
                                if (dataObject.has("details")) {

                                    JSONArray detailsArray = dataObject.getJSONArray("details");

                                    for (int k=0; k<detailsArray.length(); k++) {

                                        JSONObject detailsObject = (JSONObject) detailsArray.get(k);

                                        if (detailsObject.has("title")) {
                                            String title = detailsObject.getString("title").trim();

                                            if (!title.equalsIgnoreCase("") && title.equalsIgnoreCase(titleTag2)) {

                                                if (detailsObject.has("contents")) {

                                                    JSONArray contentsArray = detailsObject.getJSONArray("contents");

                                                    for (int l=0; l<contentsArray.length(); l++) {

                                                        JSONObject contentsObject = (JSONObject) contentsArray.get(l);

                                                        if (contentsObject.has("title")) {
                                                            String hint = contentsObject.getString("title").trim();
                                                            if (!hint.equalsIgnoreCase("")) {
                                                                MyView.setTitleView(context, hint, linearLayout, l);
                                                            }
                                                        }


                                                        if (contentsObject.has("hint")) {
                                                            String hint = contentsObject.getString("hint").trim();
                                                            if (!hint.equalsIgnoreCase("")) {
                                                                MyView.setHintView(context, hint, linearLayout, l);
                                                            }
                                                        }

                                                        if (contentsObject.has("content")) {
                                                            String content = contentsObject.getString("content").trim();
                                                            if (!content.equalsIgnoreCase("")) {
                                                                MyView.setContentView2(context, content, linearLayout, l);
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUpdatedDate(String itemTag) {
        String date = "";
        String item = "";

        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("data")) {

                    JSONArray dataArray = object.getJSONArray("data");

                    for (int j=0; j<dataArray.length(); j++) {

                        JSONObject dataObject = (JSONObject) dataArray.get(j);

                        if (dataObject.has("item")) {
                            item = dataObject.getString("item").trim();

                            if (item.equalsIgnoreCase(itemTag)) {
                                if (dataObject.has("updated_date_notice")) {
                                    date = dataObject.getString("updated_date_notice");
                                    break;
                                }
                            }

                        }
                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public boolean hasContents(String itemTag) {

        boolean contents = false;

        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("data")) {

                    JSONArray dataArray = object.getJSONArray("data");

                    for (int j=0; j<dataArray.length(); j++) {

                        JSONObject dataObject = (JSONObject) dataArray.get(j);

                        if (dataObject.has("item")) {
                            String item = dataObject.getString("item").trim();

                            if (item.equalsIgnoreCase(itemTag)) {

                                if (dataObject.has("details")) {

                                    JSONArray detailsArray = dataObject.getJSONArray("details");

                                    for (int k=0; k<detailsArray.length(); k++) {

                                        JSONObject detailsObject = (JSONObject) detailsArray.get(k);

                                        if (detailsObject.has("title")) {
                                            String title = detailsObject.getString("title").trim();
                                            if (!title.equalsIgnoreCase("")) {
                                                if (detailsObject.has("contents")) {
                                                    contents = true;
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contents;
    }

    public List<String> getSecondaryItemsList (String itemTag) {

        List<String> itemList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("data")) {

                    JSONArray dataArray = object.getJSONArray("data");

                    for (int j=0; j<dataArray.length(); j++) {

                        JSONObject dataObject = (JSONObject) dataArray.get(j);

                        if (dataObject.has("item")) {
                            String item = dataObject.getString("item").trim();

                            if (item.equalsIgnoreCase(itemTag)) {

                                if (dataObject.has("details")) {

                                    JSONArray detailsArray = dataObject.getJSONArray("details");

                                    for (int k=0; k<detailsArray.length(); k++) {

                                        JSONObject detailsObject = (JSONObject) detailsArray.get(k);

                                        if (detailsObject.has("title")) {
                                            String title = detailsObject.getString("title").trim();
                                            if (!title.equalsIgnoreCase("")) {
                                                if (detailsObject.has("contents")) {
                                                    itemList.add(title);
                                                }
                                            }
                                        }

                                    }

                                }
                            }
                        }

                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemList;
    }

    // Method for searching
    public List<String> getAllContents() {

        List<String> itemList = new ArrayList<>();
        String title="", title1="", item="", hint="", hint1="", content="", content1="";
        String tempTitle="", tempTitle1="", tempHint = "", tempHint1="";

        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("data")) {

                    JSONArray dataArray = object.getJSONArray("data");

                    for (int j = 0; j < dataArray.length(); j++) {

                        JSONObject dataObject = (JSONObject) dataArray.get(j);

                        if (dataObject.has("item")) {

                            item = dataObject.getString("item").trim();

                            if (dataObject.has("details")) {

                                JSONArray detailsArray = dataObject.getJSONArray("details");

                                for (int k = 0; k < detailsArray.length(); k++) {

                                    JSONObject detailsObject = (JSONObject) detailsArray.get(k);

                                    if (detailsObject.has("title")) {

                                        title = detailsObject.getString("title").trim();

                                        if (!title.equalsIgnoreCase("")) {

                                            if (detailsObject.has("contents")) {

                                                JSONArray contentsArray = detailsObject.getJSONArray("contents");

                                                for (int l = 0; l < contentsArray.length(); l++) {

                                                    JSONObject contentsObject = (JSONObject) contentsArray.get(l);

                                                    if (contentsObject.has("title")) {
                                                        title1 = contentsObject.getString("title").trim();
                                                    }


                                                    if (contentsObject.has("hint")) {
                                                        hint1 = contentsObject.getString("hint").trim();
                                                    }

                                                    if (contentsObject.has("content")) {
                                                        content1 = contentsObject.getString("content").trim();
                                                    }

                                                    StringBuilder sb = new StringBuilder();

                                                    if (!item.equalsIgnoreCase("")) {
                                                        sb.append(item).append("<br /> ");
                                                    }

                                                    if (!title.equalsIgnoreCase("")) {
                                                        sb.append(title).append("<br /> ");
                                                    }

                                                    if (!title1.equalsIgnoreCase("")) {
                                                        tempTitle1 = title1;
                                                        sb.append(tempTitle1).append("<br /> ");
                                                    } else {
                                                        sb.append(tempTitle1).append("<br /> ");
                                                    }

                                                    if (!hint1.equalsIgnoreCase("")) {
                                                        tempHint1 = hint1;
                                                        sb.append(tempHint1).append("<br /> ");
                                                    } else {
                                                        sb.append(tempHint1).append("<br /> ");
                                                    }

                                                    if (!content1.equalsIgnoreCase("")) {
                                                        sb.append(content1);
                                                    }

                                                    itemList.add(sb.toString());
                                                }

                                                tempTitle1 = "";
                                                tempHint1 = "";

                                            }
                                        }
                                    }

                                    if (detailsObject.has("hint")) {
                                        hint = detailsObject.getString("hint").trim();
                                    }

                                    if (detailsObject.has("content")) {
                                        content = detailsObject.getString("content").trim();
                                    }

                                    StringBuilder sb1 = new StringBuilder();

                                    if (!item.equalsIgnoreCase("")) {
                                        sb1.append(item).append("<br /> ");
                                    }

                                    if (!title.equalsIgnoreCase("")) {
                                        tempTitle = title;
                                        sb1.append(tempTitle).append("<br /> ");
                                    } else {
                                        sb1.append(tempTitle).append("<br /> ");
                                    }

                                    if (!hint.equalsIgnoreCase("")) {
                                        if (hint.contains("One") || hint.contains("Two") || hint.contains("Three") || hint.contains("Four") ||
                                                hint.contains("Five") || hint.contains("one") || hint.contains("two") || hint.contains("three") ||
                                                hint.contains("four") || !hint.contains("five") || !hint.contains("these") ||
                                                hint.contains("These") || hint.contains("nominated")) {

                                            // Nothing to do
                                            tempHint = "";

                                        } else {
                                            tempHint = hint;
                                            sb1.append(tempHint).append("<br /> ");
                                        }
                                    } else {
                                        sb1.append(tempHint).append("<br />");
                                    }

                                    if (!content.equalsIgnoreCase("")) {
                                        sb1.append(content);
                                    }

                                    itemList.add(sb1.toString());

                                }

                                tempTitle = "";
                                tempHint = "";
                            }
                        }

                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemList;
    }

}
