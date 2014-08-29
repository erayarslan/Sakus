package com.guguluk.sakus.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import com.guguluk.sakus.R;

import java.util.List;

public class Utils {
    public static void showMessage(String header, String content, Context context) {
        AlertDialog alertMessage = new AlertDialog.Builder(context).create();
        alertMessage.setTitle(header);
        alertMessage.setMessage(content);
        alertMessage.show();
    }

    public static Location getLocation(Activity activity) {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager)activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public static void visitUrl(String url, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }

    public static ProgressDialog getProgress(Activity activity) {
        return ProgressDialog.show(activity,
                activity.getString(R.string.loading_text_title),
                activity.getString(R.string.loading_text_content), true);
    }
}
