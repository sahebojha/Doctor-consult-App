package com.example.sahebojha.doctorconsult.doctors;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.example.sahebojha.doctorconsult.Constants;
import com.example.sahebojha.doctorconsult.MySingleton;
import com.example.sahebojha.doctorconsult.R;
import com.example.sahebojha.doctorconsult.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLDisplay;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPlacesDrFragment extends Fragment {


    private View view;
    private EditText name, address, pin, phone;
    private Button addPlace;

    private OnDoctorAddPlaceListener onDoctorAddPlaceListener;

    public AddPlacesDrFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_add_places_dr, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.name = (EditText) view.findViewById(R.id.place_name);
        this.address = (EditText) view.findViewById(R.id.place_address);
        this.pin = (EditText) view.findViewById(R.id.place_pin);
        this.phone = (EditText) view.findViewById(R.id.place_phone);
        this.addPlace = (Button) view.findViewById(R.id.place_add);
        this.addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddPlace(view);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onDoctorAddPlaceListener = (OnDoctorAddPlaceListener) context;

    }

    public void onAddPlace(View view) {
        addPlace.setEnabled(true);
        final int doctorId = SharedPrefManager.getInstance(getContext()).isLoggedIn();
        Toast.makeText(getActivity(), String.valueOf(doctorId), Toast.LENGTH_SHORT).show();
        onDoctorAddPlaceListener.onAddPlace(0);
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
                                int id = jsonObject.getInt("id");
                                onDoctorAddPlaceListener.onAddPlace(id);
                                Toast.makeText(getContext(), message + " : " + String.valueOf(id), Toast.LENGTH_SHORT).show();
                            }
                            else {

                                Toast.makeText(getContext(), message + " API Error", Toast.LENGTH_SHORT).show();
                            }
                            addPlace.setEnabled(true);

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
                        addPlace.setEnabled(true);

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type", "add_place");
                params.put("doctor_id", String.valueOf(doctorId));
                params.put("name", name.getText().toString());
                params.put("address", address.getText().toString());
                params.put("pin", pin.getText().toString());
                params.put("phone", phone.getText().toString());
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }


    public  interface OnDoctorAddPlaceListener {
        void onAddPlace(int id);
    }


}
