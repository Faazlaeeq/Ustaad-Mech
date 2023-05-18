package com.example.ustaadmech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class mech_info extends AppCompatActivity {
TextView name,phone,address,email,bike,car,indiv;
Button hire;
ImageView img,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mech_info);

        name=findViewById(R.id.i_mech_name);
        phone=findViewById(R.id.i_phone);
        address=findViewById(R.id.i_address);
        email=findViewById(R.id.i_email);
        bike=findViewById(R.id.i_mt_bike);
        car=findViewById(R.id.i_mt_car);
        indiv=findViewById(R.id.i_indi);
        img=findViewById(R.id.mechImg);
        back=findViewById(R.id.i_back);
        hire=findViewById(R.id.i_hire);
        Query query = FirebaseDatabase.getInstance().getReference("Mechanics").orderByChild("email").equalTo(getIntent().getStringExtra("mechEmail"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    mechModel yourModel = childSnapshot.getValue(mechModel.class);

                    name.setText(yourModel.getName());
                    email.setText(yourModel.getEmail());
                    phone.setText(yourModel.getPhone());
                    address.setText(yourModel.getAddress());
                    bike.setText(yourModel.getBike_mech());
                    car.setText(yourModel.getCar_mech());
                    String indi=yourModel.getIndividual();
                    if (yourModel.getStore()!=""&& yourModel.getStore()!=null)
                    {
                        indi=yourModel.getStore();

                    }
                    indiv.setText(indi);
                    Glide.with(mech_info.this).
                            load(yourModel.getImgurl()).
                            into(img);




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mech_info.this,SideActivity.class);
                startActivity(intent);
            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mech_info.this,hire.class);
                intent.putExtra("mechEmail",getIntent().getStringExtra("mechEmail"));
                intent.putExtra("orderid",getIntent().getStringExtra("orderid"));
                startActivity(intent);
            }
        });
    }
}