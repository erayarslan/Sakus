package com.guguluk.sakus.activity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.guguluk.sakus.R;
import com.guguluk.sakus.dto.Bus;
import com.guguluk.sakus.dto.Coordinate;
import com.guguluk.sakus.dto.Line;
import com.guguluk.sakus.resource.LineResource;
import com.guguluk.sakus.util.Utils;
import com.squareup.seismic.ShakeDetector;
import java.util.ArrayList;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BusDetailActivity extends ActionBarActivity implements ShakeDetector.Listener {

    private String lineName;
    private boolean firstTry = true;
    //
    private LineResource lineResource = new LineResource();
    private Handler customHandler = new Handler();
    //
    private TextView txtDistance;
    private GoogleMap map;
    //
    List<Marker> markerList = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);
        //
        setContentView(R.layout.activity_bus_detail);
        //
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment)).getMap();
        //
        lineName = getIntent().getExtras().getString("lineName");
        //
        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);
        //
        String[] coors = new String[0];
        try {
            coors = Utils.getRoute(lineName).split(" ");
        } catch (Exception e) {
            Utils.showMessage(getString(R.string.error), e.getMessage(), BusDetailActivity.this);
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(5);
        polylineOptions.color(Color.RED);
        polylineOptions.geodesic(true);
        for(int i=0;i<coors.length;i++) {
            String[] coor = coors[i].split(",");
            polylineOptions.add(new LatLng(Double.parseDouble(coor[1]),Double.parseDouble(coor[0])));
        }
        map.addPolyline(polylineOptions);
        //
        setTitle(lineName);
        //
        customHandler.postDelayed(refreshMap, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bus_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(BusDetailActivity.this, LineListActivity.class);
            intent.putExtra("lineName", lineName);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void hearShake() {
        Toast.makeText(this, R.string.shake_message, Toast.LENGTH_SHORT).show();
    }

    private Runnable refreshMap = new Runnable() {
        public void run() {
            fetchBusDetail();
            customHandler.postDelayed(this, Integer.parseInt(getString(R.string.refresh_interval)));
        }
    };

    private void setMapMarker(Coordinate yourCoordinate, List<Coordinate> coordinates) {
        LatLng yourLatLng = new LatLng(yourCoordinate.getLatitude(),yourCoordinate.getLongitude());
        //
        List<MarkerOptions> markerOptionsList = new ArrayList<MarkerOptions>();
        //
        for (Coordinate coordinate : coordinates) {
            LatLng latLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.visible(true);
            markerOptions.position(latLng);
            markerOptions.title(getString(R.string.title_activity_bus_detail));
            markerOptionsList.add(markerOptions);
        }
        MarkerOptions yourMarker = new MarkerOptions();
        yourMarker.visible(true);
        yourMarker.position(yourLatLng);
        yourMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        yourMarker.title(getString(R.string.you));
        //
        for (Marker marker : markerList) {
            marker.remove();
        }
        //
        map.addMarker(yourMarker);
        for(MarkerOptions markerOptions : markerOptionsList) {
            Marker marker = map.addMarker(markerOptions);
            markerList.add(marker);
        }
        //
        if(firstTry) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLatLng, 14));
        }
        if(firstTry) { firstTry = false; }
    }

    private void handleLine(Line line) {
        Float minDistance;
        Coordinate myLocation = null;
        List<Bus> buses = line.getBusList();
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (Bus bus : buses) {
            coordinates.add(bus.getCoordinate());
        }

        try {
            myLocation = Utils.getLocation(BusDetailActivity.this);
        } catch (Exception e) {
            Utils.showMessage(getString(R.string.error), e.getMessage(), BusDetailActivity.this);
        }

        if(myLocation!=null && coordinates.size()!=0) {
            minDistance = Utils.distanceTwoCoordinate(myLocation,coordinates.get(0));
            for (Coordinate coordinate : coordinates) {
                Float targetDistance = Utils.distanceTwoCoordinate(myLocation,coordinate);
                if(minDistance > targetDistance) {
                    minDistance = targetDistance;
                }
            }
            txtDistance.setText(minDistance.toString());
            setMapMarker(myLocation,coordinates);
        } else {
            txtDistance.setText(R.string.distance_error);
        }
    }

    private void fetchBusDetail() {
        lineResource.getLine(lineName, new Callback() {
            @Override
            public void failure(RetrofitError arg0) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                //
                Utils.showMessage(getString(R.string.error), arg0.getMessage(), BusDetailActivity.this);
            }

            @Override
            public void success(Object arg0, Response arg1) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                //
                handleLine((Line) arg0);
            }
        });
    };
}
