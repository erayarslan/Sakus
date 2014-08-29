package com.guguluk.sakus.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guguluk.sakus.R;
import com.guguluk.sakus.dto.Bus;

import java.util.List;

/**
 * Created by guguluk on 29.08.2014.
 */

@SuppressLint({"ViewHolder", "InflateParams"})
public class BusListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Bus> busList;

    public BusListAdapter(Activity activity, List<Bus> busList) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.busList = busList;
    }

    @Override
    public int getCount() {
        return busList.size();
    }

    @Override
    public Bus getItem(int position) {
        return busList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.row_bus_list, null);
        TextView textBusName = (TextView) rowView.findViewById(R.id.busName);

        Bus bus = busList.get(position);

        textBusName.setText(bus.getId());

        return rowView;
    }
}
