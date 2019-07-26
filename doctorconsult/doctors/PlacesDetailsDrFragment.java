package com.example.sahebojha.doctorconsult.doctors;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sahebojha.doctorconsult.Constants;
import com.example.sahebojha.doctorconsult.MySingleton;
import com.example.sahebojha.doctorconsult.R;
import com.example.sahebojha.doctorconsult.dataclass.TimeDetailsData;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesDetailsDrFragment extends Fragment
        implements TimeDetails.OnRemoveView,
        TimeDetails.OnTimePicker{


    private View view;
    private Context context;
    private LinearLayout placeDetailsHolder;
    private Button addView, submit;
    private int visitingPlaceId;
    private ArrayList<TimeDetails> timeDetailsArray = new ArrayList<>();

    OnTimeDialog onTimeDialog;

    public PlacesDetailsDrFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.visitingPlaceId = args.getInt("visiting_place_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_places_details_dr, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.placeDetailsHolder = (LinearLayout) view.findViewById(R.id.placedetail_holder);
        this.addView = (Button) view.findViewById(R.id.add_view);
        this.submit = (Button) view.findViewById(R.id.submit_place);
        final TimeDetails timeDetails = new TimeDetails(getContext());
        timeDetails.setOnRemoveView(this);
        timeDetails.setOnTimePicker(this);
        timeDetailsArray.add(timeDetails);
        placeDetailsHolder.addView(timeDetails.getView());

        this.addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addViews();
            }
        });

        this.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTimeDetails();
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        onTimeDialog = (OnTimeDialog) context;

    }

    private void addViews() {
        TimeDetails timeDetails = new TimeDetails(getContext());
        timeDetails.setOnRemoveView(this);
        timeDetails.setOnTimePicker(this);
        timeDetailsArray.add(timeDetails);
        placeDetailsHolder.addView(timeDetails.getView());

    }

    private void saveTimeDetails() {
        submit.setEnabled(true);
        int id = this.visitingPlaceId;
        ArrayList<TimeDetailsData> timeDetailsData = new ArrayList<>();
        for(int i = 0; i < timeDetailsArray.size(); i++) {
            if(timeDetailsArray.get(i).isSet()) {
                String weekday = timeDetailsArray.get(i).getWeekday();
                String time = timeDetailsArray.get(i).getTime();
                timeDetailsData.add(new TimeDetailsData(id, weekday, time));
            }
        }

        String jsonString = new Gson().toJson(timeDetailsData);

        /* TODO ***************************/
        final String finalJsonString = "{ \"timing\" :" + jsonString + "}";
        Log.d("JSON", finalJsonString);

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST,
                Constants.VISITING_PLACES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");
                            if(!error){
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                            }
                            else {

                                Toast.makeText(getContext(), message + " API Error", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage()+"catch", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage() + " VolleyError", Toast.LENGTH_SHORT).show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type", "add_time");
                params.put("timing", finalJsonString);
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }


    @Override
    public void onRemove(View view) {
        int index = placeDetailsHolder.indexOfChild(view);
        placeDetailsHolder.removeView(view);
        timeDetailsArray.remove(index);
    }

    @Override
    public void onTimePick(TimeDetails timeDetails) {
        onTimeDialog.onDialogOpen(timeDetails);
    }

    public interface OnTimeDialog {
        public void onDialogOpen(TimeDetails timeDetails);
    }

}
