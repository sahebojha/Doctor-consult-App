package com.example.sahebojha.doctorconsult.adapters;

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

public class DoctorsDataAdapter extends RecyclerView.Adapter<DoctorsDataAdapter.ViewHolder> {


    private List<DoctorsData> listItems;
    private Context context;
    private DashboardFrag.OnDoctorItemOpen onDoctorItemOpen;

    public DoctorsDataAdapter(List<DoctorsData> listItems, Context context, DashboardFrag.OnDoctorItemOpen onDoctorItemOpen) {
        this.listItems = listItems;
        this.context = context;
        this.onDoctorItemOpen = onDoctorItemOpen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                    .inflate(R.layout.doctors_item_layout, parent, false);
        return new ViewHolder(view, listItems, onDoctorItemOpen);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DoctorsData doctorsData = listItems.get(position);

        Glide.with(context).load(doctorsData.getPhoto()).into(holder.doctorImage);
        holder.doctorName.setText(doctorsData.getName());
        holder.doctorSpecialist.setText((CharSequence) doctorsData.getSpecialist());
        holder.doctorAddress.setText(doctorsData.getAddress());
        holder.doctorPin.setText(doctorsData.getPin());
        holder.doctorRatingBar.setRating(doctorsData.getRating());
        holder.doctorCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDoctorItemOpen.onItemClick(doctorsData.getId(),
                        doctorsData.getName(),
                        doctorsData.getPhoto(),
                        doctorsData.getSpecialist(),
                        doctorsData.getPhone(),
                        doctorsData.getRating(),
                        doctorsData.getMyRating());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView doctorImage;
        public TextView doctorName, doctorSpecialist, doctorAddress, doctorPin;
        public RatingBar doctorRatingBar;
        public CardView doctorCardView;

        public List<DoctorsData> listItems;
        public DashboardFrag.OnDoctorItemOpen onDoctorItemOpen;

        public ViewHolder(View itemView,List<DoctorsData> listItems, DashboardFrag.OnDoctorItemOpen onDoctorItemOpen) {
            super(itemView);

            this.listItems = listItems;
            this.onDoctorItemOpen = onDoctorItemOpen;


            doctorImage = (ImageView) itemView.findViewById(R.id.doctor_image_item);
            doctorName = (TextView) itemView.findViewById(R.id.doctor_name_item);
            doctorSpecialist = (TextView) itemView.findViewById(R.id.doctor_specialist_item);
            doctorAddress = (TextView) itemView.findViewById(R.id.doctor_address_item);
            doctorPin = (TextView) itemView.findViewById(R.id.doctor_pin_item);
            doctorRatingBar = (RatingBar) itemView.findViewById(R.id.doctor_rating_item);
            doctorCardView = (CardView) itemView.findViewById(R.id.doctor_cardview_item);


        }

        @Override
        public void onClick(View view) {
//            int position = getAdapterPosition();
//            DoctorsData doctorsData = this.listItems.get(position);
//            Toast.makeText(context, String.valueOf(doctorsData.getId()), Toast.LENGTH_SHORT).show();
//            Log.d("Hello", "hello");
//            onDoctorItemOpen.onItemClick(doctorsData.getId(),
//                    doctorsData.getName(),
//                    doctorsData.getPhoto(),
//                    doctorsData.getSpecialist(),
//                    doctorsData.getPhone());
        }
    }
}
