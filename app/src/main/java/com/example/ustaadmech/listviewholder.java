package com.example.ustaadmech;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class listviewholder extends RecyclerView.ViewHolder {
    public LinearLayout linear;
    public TextView time,name,date;
    public ImageView img;
    public listviewholder(@NonNull View itemView) {
        super(itemView);
        linear = itemView.findViewById(R.id.a_layout);
        time = itemView.findViewById(R.id.a_mech_time);
        date = itemView.findViewById(R.id.a_mech_date);
        img = itemView.findViewById(R.id.mechImg);
    }
}
