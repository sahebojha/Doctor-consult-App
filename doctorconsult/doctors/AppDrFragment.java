package com.example.sahebojha.doctorconsult.doctors;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sahebojha.doctorconsult.Constants;
import com.example.sahebojha.doctorconsult.MySingleton;
import com.example.sahebojha.doctorconsult.R;
import com.example.sahebojha.doctorconsult.SharedPrefManager;
import com.example.sahebojha.doctorconsult.adapters.AppointmentsAdapter;
import com.example.sahebojha.doctorconsult.dataclass.AppointmentsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AppDrFragment extends Fragment {
    private RecyclerView appointments;
    private List<AppointmentsData> appointmentsData = new ArrayList<>();


    public AppDrFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_dr, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appointments = (RecyclerView) view.findViewById(R.id.appointments_doctor);
        appointments.setHasFixedSize(true);
        appointments.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadAppointments();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void loadAppointments() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Appointments...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.GET_APPOINTMENTS_DOCTORS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for(int i= 0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                AppointmentsData item = new AppointmentsData(
                                        object.getInt("app_id"),
                                        object.getInt("sr_no"),
                                        object.getInt("place_id"),
                                        object.getString("place_name"),
                                        object.getInt("status"),
                                        object.getString("time"),
                                        object.getString("weekday"),
                                        object.getString("date"));
                                appointmentsData.add(item);
                            }
                            RecyclerView.Adapter adapter = new AppointmentsAdapter(appointmentsData, getContext());
                            appointments.setAdapter(adapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),e.getMessage()+"jsonexcep",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage()+"volley error",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("doctor_id", String.valueOf(SharedPrefManager.getInstance(getContext()).isLoggedIn()));
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

}
