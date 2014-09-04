package com.guguluk.sakus.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.StrictMode;

import com.bugsense.trace.BugSenseHandler;
import com.guguluk.sakus.R;
import com.guguluk.sakus.dto.Coordinate;

import org.w3c.dom.Document;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

    public static String getRoute(String lineName) throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        try {
            String url = "http://sakus.sakarya.bel.tr/Proxy/proxy.ashx?url=http%3A%2F%2Flocalhost%3A8080%2Fgeoserver%2Fwfs";
            URL obj = null;
            obj = new URL(url);
            HttpURLConnection con = null;
            con = (HttpURLConnection) obj.openConnection();
            String payload = "<wfs:GetFeature xmlns:wfs=\"http://www.opengis.net/wfs\" service=\"WFS\" version=\"1.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd\"><wfs:Query typeName=\"feature:HatCizim\" xmlns:feature=\"http://localhost:8080/SBB\"><ogc:Filter xmlns:ogc=\"http://www.opengis.net/ogc\"><ogc:And><ogc:PropertyIsEqualTo><ogc:PropertyName>NVHAT_NO</ogc:PropertyName><ogc:Literal>"+lineName+"</ogc:Literal></ogc:PropertyIsEqualTo><ogc:BBOX><ogc:PropertyName>GEOM</ogc:PropertyName><gml:Box xmlns:gml=\"http://www.opengis.net/gml\" srsName=\"EPSG:4326\"><gml:coordinates decimal=\".\" cs=\",\" ts=\" \">16.310555999999,37.476290486075 44.435555999999,44.000366126517</gml:coordinates></gml:Box></ogc:BBOX></ogc:And></ogc:Filter></wfs:Query></wfs:GetFeature>";
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.102 Safari/537.36");
            con.setUseCaches(false);
            con.setRequestProperty("Pragma","no-cache");
            con.setRequestProperty("Accept","*/*");
            con.setRequestProperty("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.6,en;q=0.4");
            con.setRequestProperty("Accept-Encoding","gzip,deflate");
            con.setRequestProperty("Origin","http://sakus.sakarya.bel.tr");
            con.setRequestProperty("Content-Type","application/xml");
            con.setRequestProperty("Cache-Control","no-cache");
            con.setRequestProperty("Referer","http://sakus.sakarya.bel.tr/");
            con.setRequestProperty("Connection","keep-alive");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream os = null;
            os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write(payload);
            writer.close();
            os.close();
            InputStream inputStream = con.getInputStream();
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            GZIPInputStream gzipInputStream = null;
            byte[] buf = new byte[1024];
            int len;
            try {
                gzipInputStream = new GZIPInputStream(inputStream);
                while ((len = gzipInputStream.read(buf)) > 0) {
                    bas.write(buf, 0, len);
                }
            }
            catch(Exception e){}
            gzipInputStream.close();
            bas.close();
            inputStream.close();

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(new ByteArrayInputStream(bas.toString().getBytes()));
            return xmlDocument.getElementsByTagName("gml:coordinates").item(0).getTextContent();
        } catch (Exception ex) {
            throw ex;
        }
    }

}
