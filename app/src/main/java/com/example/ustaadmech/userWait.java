package com.example.ustaadmech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ustaadmech.SendNotificationPack.sendnoti;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class userWait extends AppCompatActivity {
    TextView note,mechname;
    ImageView img;
    CardView f_card,name_card,full_card;
    Button cancel,call,feedback,complete;
    EditText userName,message,rate;
    FirebaseDatabase database;
    feedbackmodel m=new feedbackmodel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wait);

        img=findViewById(R.id.w_mechImg);
        Glide.with(this).
                load(getIntent().getStringExtra("imgurl")).
                into(img);


        mechname=findViewById(R.id.w_mech_name);
        userName=findViewById(R.id.f_userName);
        message=findViewById(R.id.f_message);
        feedback=findViewById(R.id.f_btn);
        complete=findViewById(R.id.o_completed);
        mechname.setText(getIntent().getStringExtra("mech_name"));
        note=findViewById(R.id.note);
        f_card=findViewById(R.id.cardfeed);
        name_card=findViewById(R.id.cardname);
        full_card=findViewById(R.id.full_card);
        rate=findViewById(R.id.rating_et);




        Query query = FirebaseDatabase.getInstance().getReference("orders").orderByKey().equalTo(getIntent().getStringExtra("id"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    hireModel yourModel = childSnapshot.getValue(hireModel.class);

                    if (yourModel.status.equals("requested"))
                    {
                        note.setText("Please wait till "+getIntent().getStringExtra("mech_name")+" responds to your Request.");

                    }
                    else if (yourModel.status.equals("coming"))
                    {
                        note.setText("Mechanic is on its way!");

                    }
                    else if (yourModel.status.equals("arrived"))
                    {
                        note.setText("Mechanic has arrived to your location!");

                    }
else if (yourModel.status.equals("declined"))
                    {
                        note.setText("Oh!, Unfortunately Mechanic has declined your Request.");

                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(userWait.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });





        cancel=findViewById(R.id.cancel);
        call=findViewById(R.id.call_mech);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userEmail=user.getEmail();
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.setUser_email(userEmail);
                m.setMech_email(getIntent().getStringExtra("mechEmail"));
                m.setMsg(message.getText().toString());
                m.setRating(rate.getText().toString());
                String id=database.getInstance().getReference().child("feedback").push().getKey();
                database.getInstance().getReference().child("feedback").child(id).setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AlertDialog.Builder adb=new AlertDialog.Builder(userWait.this);
                        adb.setTitle("Thanks For you Feedback");
                        adb.setMessage("Your feedback has been submitted.");
                        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent =new Intent(userWait.this,SideActivity.class);
                                startActivity(intent);
                            }
                        });
                        adb.show();
                    }
                });
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+getIntent().getStringExtra("phone")));
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("orders")
                        .child(getIntent().getStringExtra("id")).removeValue();
                Intent inten=new Intent(userWait.this,SideActivity.class);
                startActivity(inten);
            }
        });
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("orders")
                        .child(getIntent().getStringExtra("id")).child("status").setValue("completed");
                FirebaseDatabase.getInstance().getReference().child("orders")
                        .child(getIntent().getStringExtra("id")).child("status_email").setValue(getIntent().getStringExtra("mechEmail")+"_completed");

                sendnoti sn=new sendnoti(userWait.this);

                FirebaseDatabase.getInstance().getReference().child("Tokens").child(getIntent().getStringExtra("mechEmail").replace(".com","")).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sn.sendNotifications(snapshot.getValue().toString(),"Order Completed","");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                full_card.setVisibility(View.GONE);
                name_card.setVisibility(View.GONE);
                f_card.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Mechanics");
        String userEmail=fUser.getEmail();
        postRef.orderByChild("email").equalTo(userEmail).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            Intent newintent=new Intent(userWait.this,SideActivityMech.class);
                            newintent.putExtra("email",fUser.getEmail());
                            newintent.putExtra("id",fUser.getUid());
                            newintent.putExtra("name",fUser.getDisplayName());
                            startActivity(newintent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );


    }
    void reload()
    {
        Intent intent =new Intent(userWait.this,MainActivity.class);
        startActivity(intent);

    }
}