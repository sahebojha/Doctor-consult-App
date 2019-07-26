package com.example.sahebojha.doctorconsult;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RadioGroup usertype;
    RadioButton doctor, patient;

    EditText email, password;

    CoordinatorLayout layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (CoordinatorLayout) findViewById(R.id.layout);

        usertype = (RadioGroup) findViewById(R.id.usertype);
        doctor = (RadioButton) findViewById(R.id.doctor);
        patient = (RadioButton) findViewById(R.id.patient);
        email = (EditText)findViewById(R.id.email);
        password =(EditText)findViewById(R.id.password);

        doctor.setChecked(true);


    }



    public void register(View view) {
        startActivity(new Intent(getApplicationContext(), Register.class));
    }

    public void login(View view) {
        String email;
        String password;

        email  = this.email.getText().toString();
        password = this.password.getText().toString();
        if(email.equals("") || password.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Fill The Require Fields", Toast.LENGTH_SHORT).show();
            return;
        }
        int id;
        if((id=usertype.getCheckedRadioButtonId()) == R.id.doctor)
        {
            doctorLogin(email, password, "Doctor");
        }
        else
        {
            patientLogin(email, password, "Patient");
        }
//        Toast.makeText(getApplicationContext(), "Login!!!!!!!", Toast.LENGTH_SHORT).show();


    }

    private void patientLogin(String emailid, String pass, String user){
//        Toast.makeText(getApplicationContext(), "Patient" + email + " " + password, Toast.LENGTH_SHORT).show();
        final String email = emailid;
        final String password = pass;
        final String usertype = user;


        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST,
                Constants.PATIENT_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");
                            if(!error){
                                Snackbar.make(layout, "You are logged in...", Snackbar.LENGTH_SHORT).show();
                                int id = jsonObject.getInt("id");
                                String email = jsonObject.getString("email");
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(id, email, usertype);
                                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(intent);
                                finish();

                                //Snackbar.make(getApplicationContext(), message, Snackbar.LENGTH_SHORT).show();


                            }
                            else {
                                Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void doctorLogin(String emailid, String pass, String user){
        final String email = emailid;
        final String password = pass;
        final String usertype = user;


        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST,
                Constants.DOCTOR_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");
                            if(!error){
                                Snackbar.make(layout, "You are logged in...", Snackbar.LENGTH_SHORT).show();
                                int id = jsonObject.getInt("id");
                                String email = jsonObject.getString("email");
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(id, email, usertype);
                                Intent intent = new Intent(getApplicationContext(), DashboardDoctor.class);
                                startActivity(intent);
                                finish();

                                //Snackbar.make(getApplicationContext(), message, Snackbar.LENGTH_SHORT).show();


                            }
                            else {
                                Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
