package com.example.ustaadmech;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustaadmech.SendNotificationPack.Token;
import com.example.ustaadmech.SendNotificationPack.updatetoken;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class mechanicregister extends AppCompatActivity {
    Button apply,locate;
    String newurl;
    private FirebaseAuth mAuth;
    EditText name,email,pass,cnic,phone,address;
    Switch bike,tools;
    CheckBox car_mech,bike_mech;
    RadioButton individual,store;
    mechanic_data m=new mechanic_data();
    FusedLocationProviderClient client;
    TextView locationView;
    double longi;
    double lati;
    DatabaseReference myRef;
    StorageReference storageRef;
    ImageButton back;
    Uri imgurl;
    String uurl="";
    ImageView imageView;

    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanicregister);

        name=findViewById(R.id.mechName);
        email=findViewById(R.id.mechEmail);
        pass=findViewById(R.id.mechPass);
        cnic=findViewById(R.id.mechNIC);
        phone=findViewById(R.id.mechPhone);
        address=findViewById(R.id.mechAddress);
        bike=findViewById(R.id.mechBike);
        tools=findViewById(R.id.mechTools);
        car_mech=findViewById(R.id.mech_Car);
        bike_mech=findViewById(R.id.mech_Bike);
        individual=findViewById(R.id.mechIndi);
        store=findViewById(R.id.mechStore);
        apply=findViewById(R.id.apply_mech);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        myRef=database.getReference().child("Mechanics");
        locate=findViewById(R.id.m_d__locate_btn);
        locationView=findViewById(R.id.m_d__locate);
        imageView =findViewById(R.id.mechimg_reg);


//        name.setText("asad");
//        email.setText("asad@gmail.com");
//        cnic.setText("1212121212121");
//        phone.setText("03412394412");
//        pass.setText("123456");
//        address.setText("123456");


        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client = LocationServices.getFusedLocationProviderClient(mechanicregister.this);
                if (ActivityCompat.checkSelfPermission(mechanicregister.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    ActivityCompat.requestPermissions(mechanicregister.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                }
            }
        });


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gellaryintent=new Intent();
                gellaryintent.setAction(Intent.ACTION_GET_CONTENT);
                gellaryintent.setType("image/*");
                startActivityForResult(gellaryintent,2);

            }
        });
        back=findViewById(R.id.back);

    back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gellaryintent=new Intent(mechanicregister.this,MainActivity.class);startActivity(gellaryintent);

            }
        });

    }
    private void UpdateToken(String uid){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final String[] refreshToken = new String[1];
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        refreshToken[0] = task.getResult();
                        Token token= new Token(refreshToken[0]);
                        FirebaseDatabase.getInstance().getReference("Tokens").child(uid).setValue(token);
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        if (requestCode==2 && resultCode==RESULT_OK && data!= null)
        {
            imgurl=data.getData();
            imageView.setImageURI(imgurl);
            Toast.makeText(this, "Image Set!", Toast.LENGTH_SHORT).show();
            uplaodimage(imgurl);
        }
        else {
            Toast.makeText(this, "Image Not Set!", Toast.LENGTH_SHORT).show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void insert(){
        final String Name=name.getText().toString();
        final String Email=email.getText().toString();
        final String Cnic=cnic.getText().toString();
        final String Phone=phone.getText().toString();
        final String Pass=pass.getText().toString();
        final String Address=address.getText().toString();





        if(Name.length()==0)
        {
            name.requestFocus();
            name.setError("Please Enter Your Name");
        }
        else if(!Name.matches("[a-zA-Z ]+"))
        {
            name.requestFocus();
            name.setError("Please Enter Only Alphabetic Character");
        }

        else if(!Email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
        {
            email.requestFocus();
            email.setError("Please Enter Your Valid Email Address");
        }

        else if(Cnic.length()<=12)
        {
            cnic.requestFocus();
            cnic.setError("CNIC MUST BE OF 13 NUMBERS");
        }

        else if(!Cnic.matches("[0-9 ]+"))
        {
            cnic.requestFocus();
            cnic.setError("ENTER ONLY NUMBERS");
        }

        else if(!Phone.matches("[0-9 ]+"))
        {
            phone.requestFocus();
            phone.setError("ENTER ONLY NUMBERS");
        }

        else if(Phone.length()<=10)
        {
            phone.requestFocus();
            phone.setError("PHONE Number MUST BE OF 11 NUMBERS");
        }

        else if(Pass.length()<=5)
        {
            pass.requestFocus();
            pass.setError("Password Must Be More Than 5 Digits");
        }

        else if(Address.length()==0)
        {
            address.requestFocus();
            address.setError("Address Cannot Be Empty");
        }

        else if(imgurl!=null)
        {
            if (longi!=0.0 &&lati!=0.0 )
            {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("location");
                GeoFire geoFire = new GeoFire(ref);

                geoFire.setLocation(Email.replace(".com",""), new GeoLocation(lati, longi), new GeoFire.CompletionListener() {


                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }
                    }


                });
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(mechanicregister.this, new OnCompleteListener<AuthResult>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (task.isSuccessful()) {
                                    m.setName(name.getText().toString().toLowerCase());
                                    m.setEmail(email.getText().toString());
                                    m.setPass(pass.getText().toString());
                                    m.setAddress(address.getText().toString());
                                    m.setCnic(cnic.getText().toString());
                                    m.setPhone(phone.getText().toString());

                                    m.setImgurl(uurl);

                                    m.setStatus("0");

                                    LocalDateTime now = LocalDateTime.now();
                                    m.setTime(now.toString());

                                    if (bike.isChecked()) {
                                        String x = "Avialable";
                                        m.setBike(x);
                                    }
                                    else {m.setBike("Not Avialable");}
                                    if (tools.isChecked()) {
                                        String x = "Tools Avialable";
                                        m.setTools(x);
                                    }
                                    else {m.setTools("Not Avialable");}

                                    if (car_mech.isChecked()) {
                                        String x = "Yes";
                                        m.setCar_mech(x);
                                    }
                                    else {m.setCar_mech("No");}

                                    if (bike_mech.isChecked()) {
                                        String x = "Yes";
                                        m.setBike_mech(x);
                                    }
                                    else {m.setBike_mech("No");}

                                    if (individual.isChecked()) {
                                        String x = "Yes";
                                        m.setIndividual(x);
                                    }
                                    else {m.setIndividual("No");}

                                    if (store.isChecked()) {
                                        String x = "Yes";
                                        m.setStore(x);
                                    }
                                    else {m.setStore("No");}

                                    String id = task.getResult().getUser().getUid();

                                    database.getReference().child("MechanicsNonApproved").child(id).setValue(m);
                                    Intent intentt = new Intent(mechanicregister.this, doneRegister.class);
                                    startActivity(intentt);
                                    UpdateToken(email.getText().toString().replace(".com",""));




                                } else {
                                    Toast.makeText(mechanicregister.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            String id = task.getResult().getUser().getUid();
//
//                            database.getReference().child("MechanicsNonApproved").child(id).setValue(m);
//                            Intent intentt = new Intent(mechanicregister.this, doneRegister.class);
//                            startActivity(intentt);
                        }
                        else {
                            Toast.makeText(mechanicregister.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mechanicregister.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(mechanicregister.this,"Please Click on Get Location button.", Toast.LENGTH_SHORT).show();

            }


        }
        else
        {
            Toast.makeText(mechanicregister.this, "Please Select An Image", Toast.LENGTH_SHORT).show();
        }
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





                    Geocoder geocoder = new Geocoder(mechanicregister.this, Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(lati, longi, 1);
                        Log.d("addresss",addresses.toString());


                        Toast.makeText(mechanicregister.this, "Location Set", Toast.LENGTH_SHORT).show();

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

    public String uplaodimage(Uri url)
    {



            StorageReference fileref= storageRef.child(System.currentTimeMillis()+"."+getfileextension(url));




            fileref.putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            uurl= uri.toString();


                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mechanicregister.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                        }
                    });



                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Toast.makeText(mechanicregister.this, "failed ", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mechanicregister.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }});


            return uurl;






    }



















//    {
//
//    String ext=getfileextension(imageurl);
//        StorageReference fileref= storageRef.child(System.currentTimeMillis()+"."+ext);
//
//
//
//
//        fileref.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                newurl=fileref.getDownloadUrl().toString();
//            }
//        });
//
//
//
//        final StorageReference ref = storageRef.child("images/mountains.jpg");
//        UploadTask uploadTask = ref.putFile(imageurl);
//
//        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    throw task.getException();
//                }
//
//                // Continue with the task to get the download URL
//                return ref.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                    newurl=downloadUri.toString();
//                } else {
//                    Toast.makeText(mechanicregister.this, "Error occured!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        return newurl;
//
//    }

    private String getfileextension(Uri url)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(url));
    }
}