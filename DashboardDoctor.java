package com.example.sahebojha.doctorconsult;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.example.sahebojha.doctorconsult.doctors.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardDoctor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MyProfileDrFragment.OnDoctorEditListener,
        AddPlacesDrFragment.OnDoctorAddPlaceListener,
        PlacesDetailsDrFragment.OnTimeDialog{

    public FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction;
    Fragment mFragment;

    CircleImageView doctorImage;
    TextView doctorName, doctorEmail;
    private NavigationView navigationView;

    private final static int IMG_REQ = 121;

    private static int TIME_PICKER_ID = 0;
    public int hour_x, minute_x;

    private TimeDetails timeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_doctor);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_doctor);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view_doctor);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        doctorImage = (CircleImageView) headerView.findViewById(R.id.doctor_image);
        doctorName = (TextView) headerView.findViewById(R.id.doctor_name);
        doctorEmail = (TextView) headerView.findViewById(R.id.doctor_email);
        doctorName.setText("Doctor");

        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragment = new HomeDrFragment();
        mFragmentTransaction.add(R.id.doctor_frag_holder, mFragment);
        mFragmentTransaction.addToBackStack("DoctorDrawer");
        mFragmentTransaction.commit();
        getSupportActionBar().setTitle("Home");
        setProfileData();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_doctor);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
//            if(getSupportFragmentManager().getBackStackEntryCount() > 0) {


//            if(mFragmentManager.findFragmentById(R.id.doctor_frag_holder) instanceof Fragment) {
//                getSupportFragmentManager().popBackStack("DoctorDrawer", 0);
//                this.navigationView.setCheckedItem(R.id.nav_dr_home);
//            } else {
//                super.onBackPressed();
//            }
//        } else {
            super.onBackPressed();
//        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_doctor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.doctor_logout) {
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        mFragmentTransaction = mFragmentManager.beginTransaction();

        if (id == R.id.nav_dr_home) {
            mFragment = new HomeDrFragment();
            getSupportActionBar().setTitle("Home");
        } else if (id == R.id.nav_dr_profile) {
            mFragment = new MyProfileDrFragment();
            getSupportActionBar().setTitle("My Profile");
        } else if (id == R.id.nav_dr_appointment) {
            mFragment = new AppDrFragment();
            getSupportActionBar().setTitle("Appointments");
        }else if (id == R.id.nav_add_dr_places) {
            mFragment = new AddPlacesDrFragment();
            getSupportActionBar().setTitle("Add Visiting Places");
        }else if (id == R.id.nav_dr_places) {
            mFragment = new PlacesDrFragment();
            getSupportActionBar().setTitle("Visiting Places");
        }

        mFragmentTransaction.addToBackStack("DoctorDrawer");
        mFragmentTransaction.add(R.id.doctor_frag_holder, mFragment);
        mFragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_doctor);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQ && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();

//            Toast.makeText(getApplicationContext(), filePath.getPath(), Toast.LENGTH_SHORT).show();
            updateProfilePic(filePath);


        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == TIME_PICKER_ID) {
            return new TimePickerDialog(DashboardDoctor.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    hour_x = i;
                    minute_x = i1;
                    Toast.makeText(DashboardDoctor.this, String.valueOf(hour_x) + " : " + String.valueOf(minute_x),
                            Toast.LENGTH_SHORT).show();
                    if(timeDetails != null) {
                        timeDetails.setTime(String.valueOf(hour_x) + " : " + String.valueOf(minute_x));
                    }
                }
            }, hour_x, minute_x, false);
        }
        return null;
    }


    private void setProfileData() {
        final int id = SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn();
        Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_SHORT).show();
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST,
                Constants.DOCTOR_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");

                            if(!error){

                                String photo = jsonObject.getString("photo");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");

                                Glide.with(getApplicationContext())
                                        .load(photo)
                                        .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(doctorImage);
                                doctorName.setText(name);
                                doctorEmail.setText(email);


                            }
                            else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage()+"catch", Toast.LENGTH_SHORT).show();
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
                params.put("id", String.valueOf(id));
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onEdit(String stringResponse) {
        EditProfileDrFragment editDrProfile = new EditProfileDrFragment();
        editDrProfile.setStringResponse(stringResponse);
        mFragmentTransaction = mFragmentManager.beginTransaction();
        getSupportActionBar().setTitle("Edit Profile");
        mFragmentTransaction.add(R.id.doctor_frag_holder, editDrProfile);
        mFragmentTransaction.addToBackStack("DoctorProfile");
        mFragmentTransaction.commit();
    }

    @Override
    public void onUpdateProfilePic(View view) {
        Intent fileChooser = new Intent(Intent.ACTION_GET_CONTENT);
        fileChooser.addCategory(Intent.CATEGORY_OPENABLE);
        fileChooser.setType("image/*");
        startActivityForResult(fileChooser, IMG_REQ);
    }

    public void updateProfilePic(Uri uri){
        MyProfileDrFragment fragment = (MyProfileDrFragment) mFragmentManager.findFragmentById(R.id.doctor_frag_holder);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            fragment.updateProfilePic(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("BitmapException", e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    //On adding place details

    @Override
    public void onAddPlace(int id) {
        Bundle args = new Bundle();
        args.putInt("visiting_place_id", id);
        PlacesDetailsDrFragment placesDetailsDrFragment = new PlacesDetailsDrFragment();
        placesDetailsDrFragment.setArguments(args);
        mFragmentTransaction = mFragmentManager.beginTransaction();
        getSupportActionBar().setTitle("Place Details");
        mFragmentTransaction.add(R.id.doctor_frag_holder, placesDetailsDrFragment);
        mFragmentTransaction.addToBackStack("AddPlaces");
        mFragmentTransaction.commit();
    }

    @Override
    public void onDialogOpen(TimeDetails timeDetails) {
        this.timeDetails = timeDetails;
        showDialog(TIME_PICKER_ID);
    }



}
