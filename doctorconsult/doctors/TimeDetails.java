package com.example.sahebojha.doctorconsult.doctors;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sahebojha.doctorconsult.R;

import org.w3c.dom.Text;

import java.io.Serializable;


public class TimeDetails implements View.OnClickListener {

    private View view;
    private Context context;
    private Spinner weekday;
    private TextView time;
    private Button selectTime, removeView;


    OnRemoveView onRemoveView;
    OnTimePicker onTimePicker;


    public TimeDetails(Context context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = inflater.inflate(R.layout.time_details, null);

        this.weekday = (Spinner) view.findViewById(R.id.weekday);
        this.time = (TextView) view.findViewById(R.id.time);
        this.selectTime = (Button) view.findViewById(R.id.select_time);
        this.removeView = (Button) view.findViewById(R.id.remove_view);
        this.selectTime.setOnClickListener(this);
        this.removeView.setOnClickListener(this);
    }


    public View getView() {
        return this.view;
    }

    public void setOnRemoveView(PlacesDetailsDrFragment placesDetailsDrFragment) {
        this.onRemoveView = (OnRemoveView) placesDetailsDrFragment;
    }

    public void setOnTimePicker(PlacesDetailsDrFragment placesDetailsDrFragment) {
        this.onTimePicker = (OnTimePicker) placesDetailsDrFragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_time:
                if(this.onTimePicker != null){
                    onTimePicker.onTimePick(this);
                }
                break;

            case R.id.remove_view:
                if(this.onRemoveView != null) {
                    onRemoveView.onRemove((View) view.getParent());
                }
                break;
        }

    }

    public void setTime(String time) {
        this.time.setText(time);
        this.time.setVisibility(View.VISIBLE);
    }

    public boolean isSet() {
        return  this.time.isShown();
    }

    public String getWeekday() {
        return this.weekday.getSelectedItem().toString();
    }

    public String getTime() {
        return this.time.getText().toString();
    }

    public interface OnRemoveView {
        public void onRemove(View view);
    }

    public interface OnTimePicker{
        public void onTimePick(TimeDetails timeDetails);
    }
}
