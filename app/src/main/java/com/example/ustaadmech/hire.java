package com.example.ustaadmech;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ustaadmech.SendNotificationPack.APIService;
import com.example.ustaadmech.SendNotificationPack.Client;
import com.example.ustaadmech.SendNotificationPack.Data;
import com.example.ustaadmech.SendNotificationPack.MyResponse;
import com.example.ustaadmech.SendNotificationPack.NotificationSender;
import com.example.ustaadmech.SendNotificationPack.sendnoti;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class hire extends AppCompatActivity {
    Button Hire,locate;
    TextView name,locationView;
    ImageView img,back;
    EditText problem,address;
    hireModel m=new hireModel();
    String phone;
    FirebaseDatabase database;
    double longi;
    double lati;
    String Data[];
    String imgUrl;
    String mech_name;
    FusedLocationProviderClient client;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userEmail=user.getEmail();
        String mechEmail=getIntent().getStringExtra("mechEmail");

        name=findViewById(R.id.h_mech_name);
        img=findViewById(R.id.h_mechImg);
        problem=findViewById(R.id.h_problem);
        address=findViewById(R.id.h_address);
        Hire=findViewById(R.id.h_hire);
        locate=findViewById(R.id.h_locate_btn);
        locationView=findViewById(R.id.h_locate);
        back=findViewById(R.id.h_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(hire.this,mech_info.class);
                startActivity(intent);
            }
        });

        Toast.makeText(this, mechEmail, Toast.LENGTH_SHORT).show();

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client = LocationServices.getFusedLocationProviderClient(hire.this);
                if (ActivityCompat.checkSelfPermission(hire.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    ActivityCompat.requestPermissions(hire.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                }
            }
        });




        Query query = FirebaseDatabase.getInstance().getReference("Mechanics").orderByChild("email").equalTo(mechEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    mechModel yourModel = childSnapshot.getValue(mechModel.class);

                    name.setText(yourModel.getName());
                    mech_name=yourModel.getName();
                    phone=yourModel.getPhone();
                    imgUrl=yourModel.getImgurl();
                    Glide.with(hire.this).
                            load(yourModel.getImgurl()).
                            into(img);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.setUserEmail(userEmail);
                m.setMechEmail(mechEmail);
                m.setAddress(address.getText().toString());
                m.setStatus_email(mechEmail+"_requested");
                m.setProblem(problem.getText().toString());
                m.setStatus("requested");
                String id=database.getInstance().getReference().child("orders").push().getKey();
                database.getInstance().getReference().child("orders").child(id).setValue(m);
//                database.getReference().child("orders").setValue(m);
                sendnoti sn=new sendnoti(hire.this);

                FirebaseDatabase.getInstance().getReference().child("Tokens").child(mechEmail.replace(".com","")).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sn.sendNotifications(snapshot.getValue().toString(),"Order","You have got an order.");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                Toast.makeText(hire.this, "Your Request sent ", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(hire.this,userWait.class);
                intent.putExtra("imgurl",imgUrl);
                intent.putExtra("userEmail",userEmail);
                intent.putExtra("mechEmail",mechEmail);
                intent.putExtra("mech_name",mech_name);
                intent.putExtra("phone",phone);
                intent.putExtra("id",id);

                startActivity(intent);

            }
        });

    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    longi = location.getLongitude();
                    lati = location.getLatitude();

                    String longitude=String.valueOf(longi);
                    String latitude=String.valueOf(lati);





                    Geocoder geocoder = new Geocoder(hire.this, Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(lati, longi, 1);
                        Log.d("addresss",addresses.toString());


                        Toast.makeText(hire.this, "Location Set", Toast.LENGTH_SHORT).show();
                        m.setLongi(longitude);
                        m.setLati((latitude));
                        //getting address-----------------------------
                        //country
                        locationView.setText(addresses.get(0).getAddressLine(0).toString()+"\n");
                        //state
//                        ShowAddress.setText(addresses.get(0).getAdminArea().toString()+"\n");
                        //city
//                          ShowAddress.setText(addresses.get(0).getLocality().toString()+"\n");
                        //area
                        //   ShowAddress.setText(addresses.get(0).getSubLocality().toString()+"\n")
                        // block
                        // ShowAddress.setText(addresses.get(0).getFeatureName().toString()+"\n");
                        //Adress
//                        ShowAddress.setText(addresses.get(0).getAddressLine(0).toString()+"\n");


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void OnRequestPermissionsResult(int requestcode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (requestcode == 44) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }

    }
}