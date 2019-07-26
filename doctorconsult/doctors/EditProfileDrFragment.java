package com.example.sahebojha.doctorconsult.doctors;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.sahebojha.doctorconsult.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileDrFragment extends Fragment {


    private String stringResponse;

    private View view;
    private EditText name, phone, email, address, pin;

    public EditProfileDrFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_edit_profile_dr, container, false);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = (EditText) view.findViewById(R.id.name);
        phone = (EditText) view.findViewById(R.id.phnno);
        email = (EditText) view.findViewById(R.id.email);
        address = (EditText) view.findViewById(R.id.addrs);
        pin = (EditText) view.findViewById(R.id.pin);

        loadData();
    }

    public void setStringResponse(String stringResponse) {
        this.stringResponse = stringResponse;
    }

    public void loadData() {
        try {
            JSONObject jsonObject = new JSONObject(stringResponse);
            email.setText(jsonObject.getString("email"));
            name.setText(jsonObject.getString("name"));
            address.setText(jsonObject.getString("address"));
            pin.setText(String.valueOf(jsonObject.getInt("pin")));
            phone.setText(String.valueOf(jsonObject.getInt("phone")));
        } catch (JSONException e){

        }

    }
}
