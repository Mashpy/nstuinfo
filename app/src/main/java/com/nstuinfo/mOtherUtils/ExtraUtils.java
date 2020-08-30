package com.nstuinfo.mOtherUtils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.text.Html;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by whoami on 10/30/2018.
 */

public class ExtraUtils {

    public static int getRandomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1)) + min;
    }

    public static boolean isInternetOn(Context context) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }

        return false;
    }

    public static void appVersionUpdateNoticeDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(Html.fromHtml("<font color='#B22222'>Update App!!</font>"));
        builder.setMessage(Html.fromHtml("<font color='#000000'>New version of this app is available in the play store. We recommend you to update the app.</font>"));
        builder.setPositiveButton(Html.fromHtml("<font color='#0AA1C7'>UPDATE NOW</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExtraUtils.rateApp(activity);
                activity.finish();
                dialog.cancel();
            }
        });
        /*builder.setNegativeButton(Html.fromHtml("<font color='#0AA1C7'>LATER</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });*/

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void rateApp(Activity activity) {
        final String appPackageName = activity.getPackageName();
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

}
