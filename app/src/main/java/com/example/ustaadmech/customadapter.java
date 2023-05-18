package com.example.ustaadmech;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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
import com.example.ustaadmech.SendNotificationPack.APIService;
import com.example.ustaadmech.SendNotificationPack.Client;
import com.example.ustaadmech.SendNotificationPack.Data;
import com.example.ustaadmech.SendNotificationPack.MyFirebaseIdService;
import com.example.ustaadmech.SendNotificationPack.MyResponse;
import com.example.ustaadmech.SendNotificationPack.NotificationSender;
import com.example.ustaadmech.SendNotificationPack.Token;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customadapter extends FirebaseRecyclerAdapter <mechModel, myvieholder> {
    Context context;
    private APIService apiService;

    public customadapter(@NonNull FirebaseRecyclerOptions options,Context context)
    {
        super(options);
        this.context=context;
    }


    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

    }
    @Override
    protected void onBindViewHolder(@NonNull myvieholder holder, int position, @NonNull mechModel model1)
    {
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        holder.name.setText(model1.getName());
        holder.time.setText(model1.getTime());
        holder.date.setText(model1.getDate());
        holder.cnic.setText(model1.getCnic());

        holder.bike.setText(model1.getBike_available());
        holder.mt_car.setText(model1.getCar_mech());
        holder.tool.setText(model1.getTools());
        holder.address.setText(model1.getAddress());
        holder.mt_bike.setText(model1.getBike_mech());
        String indi=model1.getIndividual();
        if (model1.getStore()!=""&& model1.getStore()!=null)
        {
             indi=model1.getStore();

        }
        holder.indi.setText(indi);

        holder.email.setText(model1.getEmail());
        holder.phone.setText(model1.getPhone());

        Glide.with(context).
                load(model1.getImgurl()).
                into(holder.img);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Decline Panel");
                builder.setMessage("Reason for Rejection:");

                final EditText edittext = new EditText(context);


                builder.setView(edittext);



                builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {




                        FirebaseDatabase.getInstance().getReference().child("MechanicsNonApproved")
                                .child(getRef(position).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                String YouEditTextValue = edittext.getText().toString();


                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(model1.getEmail().replace(".com","")).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        sendNotifications(snapshot.getValue().toString(),"Mechanic Registration Request Declined","Reason:"+YouEditTextValue);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

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
                builder.setMessage("Do you want to Approve "+model1.getName()+" as a Mechenic?");





                builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseDatabase.getInstance().getReference().child("MechanicsNonApproved")
                                .child(getRef(position).getKey()).child("status").setValue("1").addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage()+",Please Try again later", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Query query =  FirebaseDatabase.getInstance().getReference().child("MechanicsNonApproved").orderByChild("email").equalTo(model1.getEmail());

                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    mechModel yourModel = childSnapshot.getValue(mechModel.class);


                                    FirebaseDatabase.getInstance().getReference().child("Mechanics").push().setValue(yourModel);



                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(model1.getEmail().replace(".com","")).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            sendNotifications(snapshot.getValue().toString(),"Congratulations","You are now registered as Mechanic on Ustaad Mech ,Do Sign In again!");

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
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


    }

    @NonNull
    @Override
    public myvieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate( R.layout.recyclerlayout, parent,false);
        myvieholder my=new myvieholder(view);
        return my;

    }

    @Override
    public int getItemCount()
    {
//        int id=super.getItemCount();
        return super.getItemCount();
    }

}




class myvieholder extends RecyclerView.ViewHolder{

    TextView name,date,time,cnic,bike,tool,mt_bike,mt_car,email,phone,address,indi;
    ImageView img;
    CardView card;
    Button approve,delete;
    public myvieholder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.a_mech_name);
        time=itemView.findViewById(R.id.a_mech_time);
        date=itemView.findViewById(R.id.a_mech_date);
        img=itemView.findViewById(R.id.mechImg);
        cnic=itemView.findViewById(R.id.a_cnic);
        bike=itemView.findViewById(R.id.a_bike);
        mt_car=itemView.findViewById(R.id.a_mt_car);
        tool=itemView.findViewById(R.id.a_tools);
        mt_bike=itemView.findViewById(R.id.a_mt_bike);
        email=itemView.findViewById(R.id.a_email);
        phone=itemView.findViewById(R.id.a_phone);
        indi=itemView.findViewById(R.id.a_indi);
        address=itemView.findViewById(R.id.a_address);
        delete=itemView.findViewById(R.id.del_btn);
        approve=itemView.findViewById(R.id.acc_btn);



    }
}
