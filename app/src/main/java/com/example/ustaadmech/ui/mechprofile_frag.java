package com.example.ustaadmech.ui;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ustaadmech.R;
import com.example.ustaadmech.mechanic_data;
import com.example.ustaadmech.user_sign_up;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class mechprofile_frag extends Fragment {

    public mechprofile_frag(){
        // require a empty public constructor
    }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_mechprofile_frag, container, false);

        name=v.findViewById(R.id.mechName);
        email=v.findViewById(R.id.mechEmail);
        pass=v.findViewById(R.id.mechPass);
        cnic=v.findViewById(R.id.mechNIC);
        phone=v.findViewById(R.id.mechPhone);
        address=v.findViewById(R.id.mechAddress);
        bike=v.findViewById(R.id.mechBike);
        tools=v.findViewById(R.id.mechTools);
        car_mech=v.findViewById(R.id.mech_Car);
        bike_mech=v.findViewById(R.id.mech_Bike);
        individual=v.findViewById(R.id.mechIndi);
        store=v.findViewById(R.id.mechStore);
        apply=v.findViewById(R.id.apply_mech);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        myRef=database.getReference().child("Mechanics");
        locate=v.findViewById(R.id.m_d__locate_btn);
        locationView=v.findViewById(R.id.m_d__locate);
        imageView =v.findViewById(R.id.mechimg_reg);





        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();




        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                Query userQuery = rootRef.child("Mechanics").orderByChild("email").equalTo(user.getEmail());
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", name.getText().toString());
                            map.put("cnic", cnic.getText().toString());
                            map.put("phone", phone.getText().toString());
                            map.put("address", address.getText().toString());
                            map.put("imgurl",uplaodimage(imgurl));


                            if (bike.isChecked()) {
                                String x = "Avialable";
                                map.put("bike", x);

                            }
                            else {map.put("bike","Not Avialable");}
                            if (tools.isChecked()) {
                                String x = "Tools Avialable";
                                map.put("tools", x);
                            }
                            else { map.put("tools","Not Avialable");}

                            if (car_mech.isChecked()) {
                                String x = "Yes";
                                map.put("car_mech", x);
                            }
                            else {map.put("car_mech","No");}

                            if (bike_mech.isChecked()) {
                                String x = "Yes";
                                map.put("bikemech", x);
                            }
                            else {map.put("bikemech","No");}

                            if (individual.isChecked()) {
                                String x = "Yes";
                                map.put("individual", x);
                            }
                            else {map.put("individual","No");}

                            if (store.isChecked()) {
                                String x = "Yes";
                                map.put("store", x);
                            }
                            else {map.put("store","No");}


                            ds.getRef().updateChildren(map);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
                    }
                };
                userQuery.addListenerForSingleValueEvent(valueEventListener);

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

        Query query = FirebaseDatabase.getInstance().getReference("Mechanics").orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    mechanic_data yourModel = childSnapshot.getValue(mechanic_data.class);

                    name.setText(yourModel.getName());
                    email.setText(yourModel.getEmail());
                    email.setEnabled(false);
                    cnic.setText(yourModel.getCnic());
                    phone.setText(yourModel.getPhone());
                    pass.setText(yourModel.getPass());
                    address.setText(yourModel.getAddress());
                    if (yourModel.getBike()!=null) {
                        if (yourModel.getBike().equals("Avialable")) {
                            bike.setChecked(true);
                        }
                    }
                    if (yourModel.getTools()!=null) {
                        if (yourModel.getTools().equals("Avialable")) {
                            tools.setChecked(true);
                        }                    }
                    if (yourModel.getCar_mech()!=null) {
                        if (yourModel.getCar_mech().equals("Yes")) {
                            car_mech.setChecked(true);
                        }                    }
                    if (yourModel.getBike_mech()!=null) {
                        if (yourModel.getBike_mech().equals("Yes")) {
                            bike_mech.setChecked(true);
                        }                    }
                    if (yourModel.getIndividual()!=null) {
                        if (yourModel.getIndividual().equals("Yes")) {
                            individual.setChecked(true);
                        }                    }
                    if (yourModel.getStore()!=null) {
                        if (yourModel.getStore().equals("Yes")) {
                            store.setChecked(true);
                        }
                    }




                    Glide.with(v.getContext()).
                            load(yourModel.getImgurl()).
                            into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode==2 && resultCode==RESULT_OK && data!= null)
        {
            imgurl=data.getData();
            imageView.setImageURI(imgurl);
            Toast.makeText(getContext(), "Image Set!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Image Not Set!", Toast.LENGTH_SHORT).show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private String uplaodimage(Uri url)
    {

        mechanic_data m= new mechanic_data();

        StorageReference fileref= storageRef.child(System.currentTimeMillis()+"."+getfileextension(url));


        Toast.makeText(getContext(), "uploading yayy", Toast.LENGTH_SHORT).show();


        fileref.putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        uurl= uri.toString();

                        Toast.makeText(getContext(), uurl, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });



            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(getContext(), "failed ", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(getContext(), "Completed ", Toast.LENGTH_SHORT).show();

            }
        });

        return uurl;

    }

    private String getfileextension(Uri url)
    {
        ContentResolver cr=getActivity().getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(url));
    }
}