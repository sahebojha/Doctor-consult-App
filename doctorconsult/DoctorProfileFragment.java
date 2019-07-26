package com.example.sahebojha.doctorconsult;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.sahebojha.doctorconsult.adapters.DoctorsDataAdapter;
import com.example.sahebojha.doctorconsult.adapters.PlaceDetailsAdapter;
import com.example.sahebojha.doctorconsult.adapters.TimeDataAdapter;
import com.example.sahebojha.doctorconsult.adapters.WeekdayDataAdapter;
import com.example.sahebojha.doctorconsult.dataclass.DoctorsData;
import com.example.sahebojha.doctorconsult.dataclass.PlaceDetailsData;
import com.example.sahebojha.doctorconsult.dataclass.TimeData;
import com.example.sahebojha.doctorconsult.dataclass.WeekdayData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorProfileFragment extends Fragment {
    private static final String DOCTOR_ID = "doctor_id";
    private static final String DOCTOR_NAME = "doctor_name";
    private static final String DOCTOR_IMAGE = "doctor_image";
    private static final String DOCTOR_SPECIALIST = "doctor_specialist";
    private static final String DOCTOR_PHONE = "doctor_phone";
    private static final String DOCTOR_TOTAL_RATING = "doctor_total_rating";
    private static final String DOCTOR_MY_RATING = "doctor_my_rating";
    private int doctorId;
    private int placeId;
    private String doctorName, doctorImage, doctorSpecialist, doctorPhone;
    private int selectedPlaceId;
    private String selectedWeekday, selectedTime, mainDate;

    private float totalRating, myRating;

    private View view;

    private TextView doctorNameTextView, doctorSpecialistTextView, doctorPhoneTextView, bookingDate;

    private TextView visitPlaceName, visitPlaceAddress, visitPlacePhone, visitPlacePin;

    private CircleImageView doctorImageView;

    private Spinner visitingPlaceSelect, weekdayselect, timeSelect;

    private RatingBar doctorRating;

    private Button bookAppointment;

    private LinearLayout firstHide, secondHide;


    private List<PlaceDetailsData> placeItems;
    private ArrayAdapter<PlaceDetailsData> placeItemsAdapter;

    private List<WeekdayData> weekdayItems;
    private ArrayAdapter<WeekdayData> weekdayItemsAdapter;

    private  List<TimeData> timeItems;
    private ArrayAdapter<TimeData> timeItemsAdapter;

    public DoctorProfileFragment() {
        // Required empty public constructor
    }

    public static DoctorProfileFragment newInstance(int doctorId, String doctorName, String doctorImage, String doctorSpecialist, String doctorPhone, float rating, float myRating) {
        DoctorProfileFragment fragment = new DoctorProfileFragment();
        Bundle args = new Bundle();
        args.putInt(DOCTOR_ID, doctorId);
        args.putString(DOCTOR_NAME, doctorName);
        args.putString(DOCTOR_IMAGE, doctorImage);
        args.putString(DOCTOR_SPECIALIST, doctorSpecialist);
        args.putString(DOCTOR_PHONE, doctorPhone);
        args.putFloat(DOCTOR_TOTAL_RATING, rating);
        args.putFloat(DOCTOR_MY_RATING, myRating);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctorId = getArguments().getInt(DOCTOR_ID);
            doctorName = getArguments().getString(DOCTOR_NAME);
            doctorImage = getArguments().getString(DOCTOR_IMAGE);
            doctorSpecialist = getArguments().getString(DOCTOR_SPECIALIST);
            doctorPhone = getArguments().getString(DOCTOR_PHONE);
            totalRating = getArguments().getFloat(DOCTOR_TOTAL_RATING);
            myRating = getArguments().getFloat(DOCTOR_MY_RATING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placeItems = new ArrayList<>();
        weekdayItems = new ArrayList<>();
        timeItems = new ArrayList<>();

        doctorNameTextView = (TextView) view.findViewById(R.id.doctor_name_patient);
        doctorSpecialistTextView = (TextView) view.findViewById(R.id.doctor_specialization_patient);
        doctorPhoneTextView = (TextView) view.findViewById(R.id.doctor_phone_patient);
        visitPlaceName = (TextView) view.findViewById(R.id.doctor_place_name_patient);
        visitPlaceAddress = (TextView) view.findViewById(R.id.doctor_place_address_patient);
        visitPlacePhone = (TextView) view.findViewById(R.id.doctor_place_phone_patient);
        visitPlacePin = (TextView) view.findViewById(R.id.doctor_place_pin_patient);
        bookingDate = (TextView) view.findViewById(R.id.doctor_book_date_patient);

        doctorImageView = (CircleImageView) view.findViewById(R.id.doctor_image_patient);

        visitingPlaceSelect = (Spinner) view.findViewById(R.id.doctor_place_patient);
        weekdayselect = (Spinner) view.findViewById(R.id.doctor_place_weekday_patient);
        timeSelect = (Spinner) view.findViewById(R.id.doctor_place_time_patient);

        bookAppointment = (Button) view.findViewById(R.id.doctor_book_appointment_patient);

        doctorRating = (RatingBar) view.findViewById(R.id.doctor_rating_patient);

        firstHide = (LinearLayout) view.findViewById(R.id.first_hide);
        secondHide = (LinearLayout) view.findViewById(R.id.second_hide);

        firstHide.setVisibility(View.GONE);
        secondHide.setVisibility(View.GONE);
        bookingDate.setVisibility(View.GONE);
        bookAppointment.setVisibility(View.GONE);

        loadData();

        visitingPlaceSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                PlaceDetailsData placeDetailsData = (PlaceDetailsData) adapterView.getItemAtPosition(position);
//            Toast.makeText(getContext(), placeDetailsData.getPlacePhone(), Toast.LENGTH_SHORT).show();
                Log.d("Place Name", placeDetailsData.getPlaceName());
                placeId = placeDetailsData.getId();
                selectedPlaceId = placeDetailsData.getId();
                visitPlaceName.setText(placeDetailsData.getPlaceName());
                visitPlaceAddress.setText(placeDetailsData.getPlaceAddress());
                visitPlacePin.setText(placeDetailsData.getPlacePin());
                visitPlacePhone.setText(placeDetailsData.getPlacePhone());
                loadPlaceWeekday(placeDetailsData.getId());
                firstHide.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        weekdayselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                WeekdayData weekdayData = (WeekdayData) adapterView.getItemAtPosition(position);
                selectedWeekday = weekdayData.getWeekday();
                loadPlaceTime(placeId, weekdayData.getWeekday());
                getDate();
                secondHide.setVisibility(View.VISIBLE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        timeSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                TimeData timeData = (TimeData) adapterView.getItemAtPosition(position);
                selectedTime = timeData.getTime();
//                getDate();
                bookingDate.setVisibility(View.VISIBLE);
                bookAppointment.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointmentBook();
            }
        });

        doctorRating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    final Dialog dialog = new Dialog(getActivity(), R.style.FullHeightDialog);
                    dialog.setContentView(R.layout.rating_dialog);
                    dialog.setCancelable(true);
                    final RatingBar dialogRatingBar = (RatingBar) dialog.findViewById(R.id.dialog_ratingbar);
                    dialogRatingBar.setRating(myRating);
                    TextView dialogDoctorName = (TextView) dialog.findViewById(R.id.dialog_doctor_name);
                    Button submitRating = (Button) dialog.findViewById(R.id.dialog_submit);
                    dialogDoctorName.setText(doctorName);
                    submitRating.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rateDoctor(dialogRatingBar.getRating());
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                return true;
            }
        });

    }

    private void rateDoctor(final float rating) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.GIVE_RATING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("doctor_id", String.valueOf(doctorId));
                params.put("patient_id", String.valueOf(SharedPrefManager.getInstance(getContext()).isLoggedIn()));
                params.put("rating", String.valueOf(rating));
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void loadData() {
        Glide.with(getContext()).load(doctorImage).into(doctorImageView);
        doctorNameTextView.setText(doctorName);
        doctorSpecialistTextView.setText(doctorSpecialist);
        doctorPhoneTextView.setText(doctorPhone);
        doctorRating.setRating(totalRating);

        loadPlaces();
    }

    private void loadPlaces() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.GET_PLACES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        placeItems.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for(int i= 0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    PlaceDetailsData item = new PlaceDetailsData(object.getInt("place_id"),
                                            object.getInt("doctor_id"),
                                            object.getString("place_name"),
                                            object.getString("place_address"),
                                            object.getString("place_pin"),
                                            object.getString("place_phone"));
                                    placeItems.add(item);
                                }
                                placeItemsAdapter = new PlaceDetailsAdapter(getContext(), placeItems);
//                                placeItemsAdapter.setDropDownViewResource(R.layout.place_details_select);
                                visitingPlaceSelect.setAdapter(placeItemsAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("doctor_id", String.valueOf(doctorId));

                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void loadPlaceWeekday(final int placeId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.GET_WEEKDAYS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        weekdayItems.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for(int i= 0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    WeekdayData item = new WeekdayData(object.getString("weekday"));
                                    weekdayItems.add(item);
                                }
                                weekdayItemsAdapter = new WeekdayDataAdapter(getContext(), weekdayItems);
//                                placeItemsAdapter.setDropDownViewResource(R.layout.place_details_select);
                                weekdayselect.setAdapter(weekdayItemsAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("place_id", String.valueOf(placeId));

                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void loadPlaceTime(final int placeId, final String weekday) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.GET_TIME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        timeItems.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for(int i= 0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    TimeData item = new TimeData(object.getString("time"));
                                    timeItems.add(item);
                                }
                                timeItemsAdapter = new TimeDataAdapter(getContext(), timeItems);
//                                placeItemsAdapter.setDropDownViewResource(R.layout.place_details_select);
                                timeSelect.setAdapter(timeItemsAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("place_id", String.valueOf(placeId));
                params.put("weekday", weekday);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void getDate() {
        int day = 0;
        switch(selectedWeekday) {
            case "Sunday" :
                day = Calendar.SUNDAY;
                break;
            case "Monday" :
                day = Calendar.MONDAY;
                break;
            case "Tuesday" :
                day = Calendar.TUESDAY;
                break;
            case "Wednesday" :
                day = Calendar.WEDNESDAY;
                break;
            case "Thursday" :
                day = Calendar.THURSDAY;
                break;
            case "Friday":
                day = Calendar.FRIDAY;
                break;
            case "Saturday" :
                day = Calendar.SATURDAY;
                break;
        }

        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        int totalDays = (7 + (day - today)) % 7;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        date.add(Calendar.DATE, totalDays);
        mainDate = simpleDateFormat.format(date.getTime());
        bookingDate.setText(mainDate);
        Log.d("Date", "After " + totalDays + " days, date is " + mainDate);
    }

    private void appointmentBook() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.BOOK_APPOINTMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if(!jsonObject.getBoolean("error")) {
                                int srNo = jsonObject.getInt("sr_no");
                                Toast.makeText(getContext(), message + " Your Sr No is " + String.valueOf(srNo), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("place_id", String.valueOf(selectedPlaceId));
                params.put("patient_id", String.valueOf(SharedPrefManager.getInstance(getContext()).isLoggedIn()));
                params.put("weekday", selectedWeekday);
                params.put("time", selectedTime);
                params.put("date", mainDate);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


}
