package com.nstuinfo.mConnectivity;

import android.os.AsyncTask;

import org.jsoup.Jsoup;

public class CheckUpdateAvailability extends AsyncTask<Void, String, String> {

    private String appURL;
    private UrlResponse mUrlResponse;

    public CheckUpdateAvailability (String appURL, UrlResponse callback) {
        this.appURL = appURL;
        mUrlResponse = callback;
    }

    @Override
    protected String doInBackground(Void... voids) {

        String newVersion = null;

        try {

            newVersion = Jsoup.connect(appURL)
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select(".hAyfc .htlgb")
                    .get(7)
                    .ownText();

            return newVersion;

        } catch (Exception e) {
            //Log.e("EXCEPTION", e.getLocalizedMessage());
            return newVersion;
        }

    }

    @Override
    protected void onPostExecute(String onlineVersion) {
        super.onPostExecute(onlineVersion);
        if (onlineVersion != null && !onlineVersion.isEmpty()) {
            mUrlResponse.onReceived(onlineVersion);
        }
    }
}
