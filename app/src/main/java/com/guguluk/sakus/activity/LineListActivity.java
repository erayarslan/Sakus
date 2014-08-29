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
import com.guguluk.sakus.dto.City;
import com.guguluk.sakus.dto.Line;
import com.guguluk.sakus.resource.CityResource;
import com.guguluk.sakus.util.LineListAdapter;
import com.guguluk.sakus.util.LineListComparator;
import com.guguluk.sakus.util.Utils;
import com.squareup.okhttp.internal.Util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LineListActivity extends ActionBarActivity {

    private CityResource cityResource = new CityResource();
    private ListView lineListView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_list);

        Utils.startBugSense(this);

        lineListView = (ListView) findViewById(R.id.listLines);
        progressDialog = Utils.getProgress(this);

        cityResource.getCity(new Callback() {
            @Override
            public void failure(RetrofitError arg0) {
                progressDialog.dismiss();
                //
                Utils.showMessage("Error", arg0.getMessage(), LineListActivity.this);
            }

            @Override
            public void success(Object arg0, Response arg1) {
                progressDialog.dismiss();
                //
                final City city = (City) arg0;
                final List<Line> lineList = city.getLineList();
                Collections.sort(lineList, new LineListComparator());
                LineListAdapter lineListAdapter = new LineListAdapter(LineListActivity.this, lineList);
                lineListView.setAdapter(lineListAdapter);
                lineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        Intent intent = new Intent(LineListActivity.this, BusListActivity.class);
                        intent.putExtra("lineName", lineList.get(arg2).getName());
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Utils.showMessage(getString(R.string.action_about), getString(R.string.about_info), LineListActivity.this);
            return true;
        } return super.onOptionsItemSelected(item);
    }
}
