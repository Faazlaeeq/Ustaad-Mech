package com.example.ustaadmech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class feebackadap extends FirebaseRecyclerAdapter<feedbackmodel, myholder> {
    Context context;

    public feebackadap(@NonNull FirebaseRecyclerOptions<feedbackmodel> options, feedback feedback) {
        super(options);
        this.context=context;

    }

    @Override
    protected void onBindViewHolder(@NonNull myholder holder, int position, @NonNull feedbackmodel model) {

        holder.mechem.setText(model.getMech_email());
        holder.usrem.setText(model.getUser_email());
        holder.msgg.setText(model.getMsg());
        holder.rate.setText(model.getRating());

    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate( R.layout.feedbackrecycler, parent,false);
        myholder my=new myholder(view);
        return my;    }
    @Override
    public int getItemCount()
    {
//        int id=super.getItemCount();
        return super.getItemCount();
    }
}
class myholder extends RecyclerView.ViewHolder{

    TextView usrem,mechem,msgg,rate;
    public myholder(@NonNull View itemView) {
        super(itemView);
        usrem=itemView.findViewById(R.id.usrem);
        mechem=itemView.findViewById(R.id.mechem);
        msgg=itemView.findViewById(R.id.msgg);
        rate=itemView.findViewById(R.id.rate_tv);




    }
}
