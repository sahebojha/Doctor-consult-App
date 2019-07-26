package com.example.sahebojha.doctorconsult;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity
                implements NavigationView.OnNavigationItemSelectedListener,
                            MyProfileFrag.OnEditListener,
                            DashboardFrag.OnDoctorItemOpen{


    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction;
    Fragment mFragment;
    private final int IMG_REQ = 222;

    private CircleImageView patientImage;
    private TextView patientName;
    private TextView patientEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        patientImage = (CircleImageView) header.findViewById(R.id.patient_image);
        patientName = (TextView) header.findViewById(R.id.patient_name);
        patientEmail = (TextView) header.findViewById(R.id.patient_email);

        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragment = new DashboardFrag();
        mFragmentTransaction.add(R.id.fragment_holder, mFragment);
        getSupportActionBar().setTitle("Dashboard");
//        mFragmentTransaction.addToBackStack("navigation_item");
        mFragmentTransaction.commit();


        permissions();

        setProfileData();

    }

    private void permissions() {
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, readPermission);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
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

        if (id == R.id.nav_dashboard) {
            mFragment = new DashboardFrag();
            getSupportActionBar().setTitle("Dashboard");
        } else if (id == R.id.nav_book_appointment) {
            mFragment = new BookAppFrag();
            getSupportActionBar().setTitle("Book Appointment");
        } else if (id == R.id.nav_my_appointments) {
            mFragment = new MyAppFrag();
            getSupportActionBar().setTitle("My Appointments");
        } else if (id == R.id.nav_my_profile) {
            mFragment = new MyProfileFrag();
            getSupportActionBar().setTitle("My Profile");
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_contact_us) {

        }

        mFragmentTransaction.replace(R.id.fragment_holder, mFragment);
//        mFragmentTransaction.addToBackStack("navigation_item");
        mFragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    private void setProfileData() {
        final int id = SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn();
        Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_SHORT).show();
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST,
                Constants.PATIENT_PROFILE,
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
                                        .into(patientImage);
                                patientName.setText(name);
                                patientEmail.setText(email);


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
        EditProfileFrag editProfile = new EditProfileFrag();
        editProfile.setStringResponse(stringResponse);
        mFragmentTransaction = mFragmentManager.beginTransaction();
        getSupportActionBar().setTitle("Edit Profile");
        mFragmentTransaction.add(R.id.fragment_holder, editProfile);
        mFragmentTransaction.addToBackStack("Profile");
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
        MyProfileFrag fragment = (MyProfileFrag) mFragmentManager.findFragmentById(R.id.fragment_holder);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            fragment.updateProfilePic(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("BitmapException", e.getMessage());
        }

    }
    @Override
    public void onItemClick(int id, String name, String image, String specialist, String phone, float rating, float myRating) {
        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
        DoctorProfileFragment doctorProfileFragment = DoctorProfileFragment.newInstance(id, name, image, specialist, phone, rating, myRating);
        getSupportActionBar().setTitle(name);
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.fragment_holder, doctorProfileFragment);
        mFragmentTransaction.addToBackStack("Doctors");
        mFragmentTransaction.commit();
    }
}
