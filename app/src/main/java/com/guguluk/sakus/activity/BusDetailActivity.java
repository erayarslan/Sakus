package com.guguluk.sakus.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.guguluk.sakus.R;
import com.guguluk.sakus.util.Utils;

public class BusDetailActivity extends ActionBarActivity {

    private String lineName;
    private String busId;

    private TextView txtDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);

        Utils.startBugSense(this);

        txtDistance = (TextView) findViewById(R.id.txtDistance);

        lineName = getIntent().getExtras().getString("lineName");
        busId = getIntent().getExtras().getString("busId");

        txtDistance.setText("TEST-0.0");
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
