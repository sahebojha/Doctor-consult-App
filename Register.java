package com.example.sahebojha.doctorconsult;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText
            name, email, addrs;
    private EditText
            password, confirmPassword, phnno, pin, aadhar;
    private Button btn;
    private Spinner state, specialist;

    private RadioGroup usertype, gender;
    private RadioButton doctor, patient, male, female;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.hide();

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        addrs = (EditText) findViewById(R.id.addrs);
        state = (Spinner) findViewById(R.id.state);
        phnno = (EditText) findViewById(R.id.phnno);
        pin = (EditText) findViewById(R.id.pin);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        btn = (Button) findViewById(R.id.next);
        aadhar = (EditText) findViewById(R.id.aadhar);
        specialist = (Spinner) findViewById(R.id.specialists);

        usertype = (RadioGroup) findViewById(R.id.usertype);
        gender = (RadioGroup) findViewById(R.id.gender);
        doctor = (RadioButton) findViewById(R.id.doctor);
        patient = (RadioButton) findViewById(R.id.patient);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        doctor.setChecked(true);
        male.setChecked(true);
        aadhar.setVisibility(View.VISIBLE);
        specialist.setVisibility(View.VISIBLE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.state, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       int id = usertype.getCheckedRadioButtonId();
                                       if(id == doctor.getId())
                                       {
                                           registerDoctor();

                                       }
                                       else
                                           registerPatient();
                                   }
                               }


        );

        usertype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.doctor:
                        btn.setText("Next");
                        aadhar.setVisibility(View.VISIBLE);
                        specialist.setVisibility(View.VISIBLE);
                        break;
                    case R.id.patient:
                        btn.setText("Submit");
                        aadhar.setVisibility(View.GONE);
                        specialist.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    void registerPatient(){
        final String name = this.name.getText().toString();
        final String email = this.email.getText().toString();
        final String phone = this.phnno.getText().toString();
        final String address = this.addrs.getText().toString();
        final String pin = this.pin.getText().toString();
        String state = this.state.getSelectedItem().toString();
        final String gender;
        if(this.gender.getCheckedRadioButtonId() == R.id.male)
            gender = "Male";
        else
            gender = "Female";

        final String password = this.password.getText().toString();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST,
                Constants.PATIENT_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("name", name);
                params.put("password", password);
                params.put("phone", phone);
                params.put("address", address);
                params.put("pin", pin);
                params.put("gender", gender);
                return params;
            }
        };


        progressDialog.show();
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
//        Volley.newRequestQueue(this).add(stringRequest);
    }

    void registerDoctor(){
        final String name = this.name.getText().toString();
        final String email = this.email.getText().toString();
        final String phone = this.phnno.getText().toString();
        final String address = this.addrs.getText().toString();
        final String pin = this.pin.getText().toString();
        String state = this.state.getSelectedItem().toString();
        final String gender;
        if(this.gender.getCheckedRadioButtonId() == R.id.male)
            gender = "Male";
        else
            gender = "Female";

        final String password = this.password.getText().toString();
        final String aadhar = this.aadhar.getText().toString();
        final String specialist = this.specialist.getSelectedItem().toString();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST,
                Constants.DOCTOR_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String message = jsonObject.getString("message");
                            boolean error = jsonObject.getBoolean("error");
                            if(!error){
                                int id = jsonObject.getInt("id");


                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), DoctorDetails.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                                finish();
                            }
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("name", name);
                params.put("password", password);
                params.put("phone", phone);
                params.put("address", address);
                params.put("pin", pin);
                params.put("gender", gender);
                params.put("aadhar", aadhar);
                params.put("specialist", specialist);
                return params;
            }
        };


        progressDialog.show();
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
//        Volley.newRequestQueue(this).add(stringRequest);
    }
}
