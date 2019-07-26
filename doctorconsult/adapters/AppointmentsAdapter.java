package com.example.sahebojha.doctorconsult.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.sahebojha.doctorconsult.R;
import com.example.sahebojha.doctorconsult.dataclass.AppointmentsData;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sahebojha.doctorconsult.DashboardFrag;
import com.example.sahebojha.doctorconsult.DoctorDetails;
import com.example.sahebojha.doctorconsult.R;
import com.example.sahebojha.doctorconsult.dataclass.DoctorsData;

import org.w3c.dom.Text;

import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {


    private List<AppointmentsData> listItems;
    private Context context;
//    private DashboardFrag.OnDoctorItemOpen onDoctorItemOpen;

    public AppointmentsAdapter(List<AppointmentsData> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
//        this.onDoctorItemOpen = onDoctorItemOpen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.appointments_layout, parent, false);
        return new ViewHolder(view, listItems);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String sStatus = "Null";
        final AppointmentsData appointmentsData = listItems.get(position);

        holder.srNoTv.setText(String.valueOf(appointmentsData.getSrNo()));
        holder.placeNameTv.setText(appointmentsData.getPlaceName());
        holder.dateTv.setText(appointmentsData.getDate());

        switch (appointmentsData.getStatus()) {
            case 0:
                sStatus = "Canceled By Doctor";
                break;
            case 1:
                sStatus = "Active";
                break;
            case 2:
                sStatus = "Done";
                break;
            case 3:
                sStatus = "Canceled by Patient";
                break;


        }
        holder.statusTv.setText(sStatus);
//        holder.doctorCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onDoctorItemOpen.onItemClick(doctorsData.getId(),
//                        doctorsData.getName(),
//                        doctorsData.getPhoto(),
//                        doctorsData.getSpecialist(),
//                        doctorsData.getPhone(),
//                        doctorsData.getRating(),
//                        doctorsData.getMyRating());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView srNoTv, placeNameTv, dateTv, statusTv;
        Button action;

        public List<AppointmentsData> listItems;
//        public DashboardFrag.OnDoctorItemOpen onDoctorItemOpen;

        public ViewHolder(View itemView,List<AppointmentsData> listItems) {
            super(itemView);

            this.listItems = listItems;



            srNoTv = (TextView) itemView.findViewById(R.id.app_srno);
            placeNameTv = (TextView) itemView.findViewById(R.id.app_place_name);
            dateTv = (TextView) itemView.findViewById(R.id.app_date);
            statusTv = (TextView) itemView.findViewById(R.id.app_status);
            action = (Button) itemView.findViewById(R.id.app_action);


        }


    }
}

