package com.example.rest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rest.R;
import com.example.rest.models.Reservation;
import com.example.rest.models.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    Context context;
    List<Reservation> reservations;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    public BookingAdapter(Context context, List<Reservation> reservations) {
        this.context = context;
        this.reservations = reservations;
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("reservations");
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reservation, parent, false);
        return new BookingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookingAdapter.ViewHolder holder, int position) {
        final Reservation reservation = reservations.get(position);
        final String key = reservation.getEmail().replace(".","") + "|" + reservation.getDate().replace("/","") + "|" + reservation.getTime().replace(":","");

        holder.txtEmail.setText(reservation.getEmail());
        holder.txtDate.setText(reservation.getDate());
        holder.txtTime.setText(reservation.getTime());
        holder.txtGuests.setText(reservation.getNumberGuests());
        holder.txtTable.setText(reservation.getTablePreference());
        holder.txtRequest.setText(reservation.getSpecialRequests());
        holder.txtConfirmed.setText(reservation.getConfirm());

        if(mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail().equals("martina_trifunoska@hotmail.com")){
            holder.txtConfirmed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.txtConfirmed.getText().toString().equals("NO")) {
                        databaseReference.child(key).child("confirm").setValue("YES");
                        holder.txtConfirmed.setText("YES");
                    }
                    else{
                        databaseReference.child(key).child("confirm").setValue("NO");
                        holder.txtConfirmed.setText("NO");
                    }
                }
            });
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(key).removeValue();
                reservations.remove(reservation);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public void updateData(List<Reservation> reservations){
        this.reservations = reservations;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtEmail, txtDate, txtTime, txtGuests, txtTable, txtRequest, txtConfirmed, deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtGuests = itemView.findViewById(R.id.txtGuests);
            txtTable = itemView.findViewById(R.id.txtTable);
            txtRequest = itemView.findViewById(R.id.txtRequest);
            txtConfirmed = itemView.findViewById(R.id.txtConfirmed);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
