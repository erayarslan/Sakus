package com.guguluk.sakus.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.guguluk.sakus.R;
import com.guguluk.sakus.dto.Bus;
import com.guguluk.sakus.dto.Coordinate;
import com.guguluk.sakus.resource.BusResource;
import com.guguluk.sakus.util.Utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BusDetailActivity extends ActionBarActivity {

    private BusResource busResource = new BusResource();

    private String lineName;
    private String busId;

    private TextView txtDistance;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);

        Utils.startBugSense(this);

        txtDistance = (TextView) findViewById(R.id.txtDistance);

        lineName = getIntent().getExtras().getString("lineName");
        busId = getIntent().getExtras().getString("busId");

        progressDialog = Utils.getProgress(this);

        busResource.getBus(lineName, busId, new Callback() {
            @Override
            public void failure(RetrofitError arg0) {
                progressDialog.dismiss();
                //
                Utils.showMessage("Error", arg0.getMessage(), BusDetailActivity.this);
            }

            @Override
            public void success(Object arg0, Response arg1) {
                progressDialog.dismiss();
                //
                final Bus bus = (Bus) arg0;
                Coordinate myLocation = null;
                Coordinate busLocation = bus.getCoordinate();
                try {
                    myLocation = Utils.getLocation(BusDetailActivity.this);
                } catch (Exception e) {
                    Utils.showMessage("Error", e.getMessage(), BusDetailActivity.this);
                }

                if(myLocation!=null && busLocation!=null) {
                    Float distance = Utils.distanceTwoCoordinate(myLocation,busLocation);
                    txtDistance.setText(distance.toString());
                } else {
                    txtDistance.setText(":/");
                }
            }
        });
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
}
