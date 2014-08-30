package com.guguluk.sakus.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import com.bugsense.trace.BugSenseHandler;
import com.guguluk.sakus.R;
import com.guguluk.sakus.dto.Coordinate;

import java.util.List;

public class Utils {
    public static void showMessage(String header, String content, Context context) {
        AlertDialog alertMessage = new AlertDialog.Builder(context).create();
        alertMessage.setTitle(header);
        alertMessage.setMessage(content);
        alertMessage.show();
    }

    public static Coordinate getLocation(Activity activity) throws Exception {
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

        if(bestLocation == null) {
            throw new Exception(activity.getString(R.string.location_not_found));
        }

        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(bestLocation.getLatitude());
        coordinate.setLongitude(bestLocation.getLongitude());

        return coordinate;
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

    public static void startBugSense(Activity activity) {
        BugSenseHandler.initAndStartSession(activity, activity.getString(R.string.bugSense));
    }

    public static float distanceTwoCoordinate(Coordinate from, Coordinate to) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(to.getLatitude()-from.getLatitude());
        double dLng = Math.toRadians(to.getLongitude()-from.getLongitude());
        double a =  Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(from.getLatitude())) * Math.cos(Math.toRadians(to.getLatitude())) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        int meterConversion = 1609;

        return (float) (dist * meterConversion);
    }

}
