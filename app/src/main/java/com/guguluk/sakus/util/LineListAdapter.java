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
import com.guguluk.sakus.dto.Line;

import java.util.List;

/**
 * Created by guguluk on 29.08.2014.
 */

@SuppressLint({"ViewHolder", "InflateParams"})
public class LineListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Line> lineList;

    public LineListAdapter(Activity activity, List<Line> lineList) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lineList = lineList;
    }

    @Override
    public int getCount() {
        return lineList.size();
    }

    @Override
    public Line getItem(int position) {
        return lineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.row_line_list, null);
        TextView textLineName = (TextView) rowView.findViewById(R.id.lineName);

        Line line = lineList.get(position);

        textLineName.setText(line.getName());

        return rowView;
    }
}
