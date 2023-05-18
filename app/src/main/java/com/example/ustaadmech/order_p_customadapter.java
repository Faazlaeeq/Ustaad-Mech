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

public class order_p_customadapter extends FirebaseRecyclerAdapter <hireModel, order_p_myvieholder> {
    Context context;
    public order_p_customadapter(@NonNull FirebaseRecyclerOptions options,Context context)
    {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull order_p_myvieholder holder, int position, @NonNull hireModel model1)
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
                et.setPadding(5,5,5,5);
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
                                sn.sendNotifications(snapshot.getValue().toString(),"Order Canceled","Reason:"+et.getText().toString());

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


                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Approve");
                builder.setMessage("Are you at user's location?");




                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseDatabase.getInstance().getReference().child("orders")
                                .child(getRef(position).getKey()).child("status").setValue("arrived");
                        FirebaseDatabase.getInstance().getReference().child("orders")
                                .child(getRef(position).getKey()).child("status_email").setValue(model1.getMechEmail()+"_arrived");

                        sendnoti sn=new sendnoti(context);

                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(model1.getUserEmail().replace(".com","")).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                sn.sendNotifications(snapshot.getValue().toString(),"Mechacnic Arrived","Mechanic has arrived to you location.");

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        Intent intent=new Intent(context,SideActivityMech.class);
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
    public order_p_myvieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate( R.layout.orderrecyclerlayout, parent,false);
        order_p_myvieholder my=new order_p_myvieholder(view);
        return my;

    }

    @Override
    public int getItemCount()
    {
//        int id=super.getItemCount();
        return super.getItemCount();
    }



}


class order_p_myvieholder extends RecyclerView.ViewHolder{

    TextView name,problem,time,cnic,bike,tool,mt_bike,mt_car,email,phone,address,indi;
    ImageView img;
    CardView card;
    Button approve,delete,getloaction;
    public order_p_myvieholder(@NonNull View itemView) {
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
        approve.setText("Arrived!");


    }
}
