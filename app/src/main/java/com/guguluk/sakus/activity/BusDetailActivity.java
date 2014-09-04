package com.guguluk.sakus.activity;

import android.content.Intent;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.guguluk.sakus.R;
import com.guguluk.sakus.dto.Bus;
import com.guguluk.sakus.dto.Coordinate;
import com.guguluk.sakus.resource.BusResource;
import com.guguluk.sakus.util.Utils;
import com.squareup.seismic.ShakeDetector;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BusDetailActivity extends ActionBarActivity implements ShakeDetector.Listener {

    private String lineName;
    private String busId;
    private boolean firstTry = true;
    //
    private BusResource busResource = new BusResource();
    private Handler customHandler = new Handler();
    //
    private TextView txtDistance;
    private GoogleMap map;

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
        busId = getIntent().getExtras().getString("busId");
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
            Intent intent = new Intent(BusDetailActivity.this, BusListActivity.class);
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

    private void setMapMarker(Coordinate yourCoordinate, Coordinate busCoordinate) {
        LatLng yourLatLng = new LatLng(yourCoordinate.getLatitude(),yourCoordinate.getLongitude());
        LatLng busLatLng = new LatLng(busCoordinate.getLatitude(),busCoordinate.getLongitude());
        //
        MarkerOptions busMarker = new MarkerOptions();
        busMarker.visible(true);
        busMarker.position(busLatLng);
        busMarker.title(lineName);
        //
        MarkerOptions yourMarker = new MarkerOptions();
        yourMarker.visible(true);
        yourMarker.position(yourLatLng);
        yourMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        yourMarker.title(getString(R.string.you));
        //
        map.clear();
        map.addMarker(busMarker);
        map.addMarker(yourMarker);
        //
        if(firstTry) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(busLatLng, 14));
        }
        if(firstTry) { firstTry = false; }
    }

    private void handleBus(Bus bus) {
        Coordinate myLocation = null;
        Coordinate busLocation = bus.getCoordinate();
        try {
            myLocation = Utils.getLocation(BusDetailActivity.this);
        } catch (Exception e) {
            Utils.showMessage(getString(R.string.error), e.getMessage(), BusDetailActivity.this);
        }

        if(myLocation!=null && busLocation!=null) {
            Float distance = Utils.distanceTwoCoordinate(myLocation,busLocation);
            txtDistance.setText(distance.toString());
            setMapMarker(myLocation,bus.getCoordinate());
        } else {
            txtDistance.setText(R.string.distance_error);
        }
    }

    private void fetchBusDetail() {
        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);
        //
        busResource.getBus(lineName, busId, new Callback() {
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
                handleBus((Bus) arg0);
            }
        });
    };
}
