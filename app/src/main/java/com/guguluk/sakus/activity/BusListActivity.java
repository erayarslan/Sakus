package com.guguluk.sakus.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.guguluk.sakus.R;
import com.guguluk.sakus.dto.Bus;
import com.guguluk.sakus.dto.Line;
import com.guguluk.sakus.resource.LineResource;
import com.guguluk.sakus.util.BusListAdapter;
import com.guguluk.sakus.util.Utils;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BusListActivity extends ActionBarActivity {

    private LineResource lineResource = new LineResource();
    private ListView busListView;
    private ProgressDialog progressDialog;

    private String lineName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        busListView = (ListView) findViewById(R.id.busLines);
        progressDialog = Utils.getProgress(this);

        lineName = getIntent().getExtras().getString("lineName");

        lineResource.getLine(lineName,new Callback() {
            @Override
            public void failure(RetrofitError arg0) {
                progressDialog.dismiss();
                //
                Utils.showMessage("Error", arg0.getMessage(), BusListActivity.this);
            }

            @Override
            public void success(Object arg0, Response arg1) {
                progressDialog.dismiss();
                //
                final Line line = (Line) arg0;
                final List<Bus> busList = line.getBusList();
                BusListAdapter busListAdapter = new BusListAdapter(BusListActivity.this, busList);
                busListView.setAdapter(busListAdapter);
                busListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        Intent intent = new Intent(BusListActivity.this, BusDetailActivity.class);
                        intent.putExtra("lineName", lineName);
                        intent.putExtra("busId", busList.get(arg2).getId());
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bus_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
