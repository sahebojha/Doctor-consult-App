package com.example.sahebojha.doctorconsult;

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
import com.example.sahebojha.doctorconsult.adapters.DoctorsDataAdapter;
import com.example.sahebojha.doctorconsult.dataclass.DoctorsData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DashboardFrag extends Fragment {


    private RecyclerView doctorsList;
    private RecyclerView.Adapter adapter;

    private List<DoctorsData> listItems;

    public OnDoctorItemOpen onDoctorItemOpen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doctorsList = (RecyclerView) view.findViewById(R.id.doctors_list);
        doctorsList.setHasFixedSize(true);
        doctorsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItems = new ArrayList<>();
        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Doctors...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.GET_DOCTORS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for(int i= 0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                DoctorsData item = new DoctorsData(object.getInt("doctor_id"),
                                                                    object.getString("name"),
                                                                    object.getString("email"),
                                                                    object.getString("photo"),
                                                                    object.getString("address"),
                                                                    object.getString("specialist"),
                                                                    object.getString("phone"),
                                                                    object.getString("pin"),
                                                                    (float) object.getDouble("rating"),
                                                                    (float) object.getDouble("my_rating"));
                                listItems.add(item);
                            }
                            adapter = new DoctorsDataAdapter(listItems,getContext(), onDoctorItemOpen);
                            doctorsList.setAdapter(adapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("patient_id", String.valueOf(SharedPrefManager.getInstance(getContext()).isLoggedIn()));
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onDoctorItemOpen = (OnDoctorItemOpen) context;

    }

    public interface OnDoctorItemOpen {
        void onItemClick(int id, String name, String image, String specialist, String phone, float rating, float myRating);
    }

}
