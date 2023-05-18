package com.example.ustaadmech;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ustaadmech.SendNotificationPack.sendnoti;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ordercustomadapter extends FirebaseRecyclerAdapter <hireModel, ordermyvieholder> {
    Context context;
    public ordercustomadapter(@NonNull FirebaseRecyclerOptions options,Context context)
    {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ordermyvieholder holder, int position, @NonNull hireModel model1)
    {

        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("email").equalTo(model1.getUserEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    user_sign_up yourModel = childSnapshot.getValue(user_sign_up.class);

                    holder.name.setText(yourModel.getName());
                    Glide.with(context).
                            load(yourModel.getImgurl()).
                            into(holder.img);

                    holder.phone.setText(yourModel.getPhone());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });




        holder.address.setText(model1.getAddress());


        holder.email.setText(model1.getUserEmail());
        holder.problem.setText(model1.getProblem());

        holder.getloaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+model1.lati+","+model1.longi);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Are you sure you want to Decline this request...?");
                EditText et=new EditText(context);
                builder.setView(et);



                builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {




                        FirebaseDatabase.getInstance().getReference().child("orders")
                                .child(getRef(position).getKey()).child("status").setValue("declined");

                        FirebaseDatabase.getInstance().getReference().child("orders")
                                .child(getRef(position).getKey()).child("status_email").setValue(model1.getUserEmail()+"_declined");
                        sendnoti sn=new sendnoti(context);

                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(model1.getUserEmail().replace(".com","")).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                sn.sendNotifications(snapshot.getValue().toString(),"Order Declined","Reason:"+et.getText().toString());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();



            }
        });

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Approve");
                builder.setMessage("If you accept this request you sholud reach at location as soon as possible. Do you wnat to approve?");




                builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseDatabase.getInstance().getReference().child("orders")
                                .child(getRef(position).getKey()).child("status").setValue("coming");
                        FirebaseDatabase.getInstance().getReference().child("orders")
                                .child(getRef(position).getKey()).child("status_email").setValue(model1.getMechEmail()+"_coming");
                        sendnoti sn=new sendnoti(context);

                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(model1.getUserEmail().replace(".com","")).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                sn.sendNotifications(snapshot.getValue().toString(),"Order Accepted","Mechanic has accepted your order.");

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Intent intent=new Intent(context,orderpending.class);
                        context.startActivity(intent);
                    }
                });

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();



            }
        });


    }

    @NonNull
    @Override
    public ordermyvieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate( R.layout.orderrecyclerlayout, parent,false);
        ordermyvieholder my=new ordermyvieholder(view);
        return my;

    }

    @Override
    public int getItemCount()
    {
//        int id=super.getItemCount();
        return super.getItemCount();
    }



}


class ordermyvieholder extends RecyclerView.ViewHolder{

    TextView name,problem,time,cnic,bike,tool,mt_bike,mt_car,email,phone,address,indi;
    ImageView img;
    CardView card;
    Button approve,delete,getloaction;
    public ordermyvieholder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.o_mech_name);
        img=itemView.findViewById(R.id.o_mechImg);
        email=itemView.findViewById(R.id.o_email);
        phone=itemView.findViewById(R.id.o_phone);
        address=itemView.findViewById(R.id.o_address);
        delete=itemView.findViewById(R.id.del_btn);
        approve=itemView.findViewById(R.id.acc_btn);
        getloaction=itemView.findViewById(R.id.o_locate);
        problem=itemView.findViewById(R.id.o_problem);
        approve.setText("Accept");



    }
}
