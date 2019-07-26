package com.example.sahebojha.doctorconsult.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sahebojha.doctorconsult.R;
import com.example.sahebojha.doctorconsult.dataclass.TimeData;
import com.example.sahebojha.doctorconsult.dataclass.WeekdayData;

import java.util.List;

public class TimeDataAdapter extends ArrayAdapter<TimeData> {
    private List<TimeData> listItems;
    private Context context;

    public TimeDataAdapter(@NonNull Context context, @NonNull List<TimeData> objects) {
        super(context, 0, objects);
        this.listItems = objects;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView v = (TextView) getView(position, convertView, parent);
        if(v == null) {
            v = new TextView(context);
        }
        v.setTextColor(Color.BLACK);
        v.setTextSize(18f);
        v.setText(listItems.get(position).getTime());
        return v;
    }

    @Override
    public TimeData getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TimeData timeData = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_layout, parent, false);
        }

        TextView placeName = (TextView) convertView.findViewById(R.id.custom_spinner_text);
        if (timeData != null) {
            placeName.setText(timeData.getTime());
        } else {
            Log.d("NullPointer", "Null pointer at WeekdayDataAdapter");
        }

        return convertView;
    }
}
