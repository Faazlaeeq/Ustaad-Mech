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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ustaadmech.R;
import android.content.ContentResolver;

import com.example.ustaadmech.mechanic_data;
import com.example.ustaadmech.mechanicregister;
import com.example.ustaadmech.user_sign_up;
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

public class profile_frag extends Fragment {

    public profile_frag(){
        // require a empty public constructor
    }
    EditText name,email,address,cnic,phone;
    Button save;
    StorageReference storageRef;

    Uri imgurl;
    String uurl;
    ImageView imageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_profile_frag, container, false);
        name=v.findViewById(R.id.user_p_name);
        email=v.findViewById(R.id.user_p_email);
        save=v.findViewById(R.id.save_user);
        cnic=v.findViewById(R.id.user_p_cnic);
        phone=v.findViewById(R.id.user_p_phone);
        address=v.findViewById(R.id.user_p_address);
        imageView =v.findViewById(R.id.profileimg);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                Query userQuery = rootRef.child("users").orderByChild("email").equalTo(user.getEmail());
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

        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    user_sign_up yourModel = childSnapshot.getValue(user_sign_up.class);

                    name.setText(yourModel.getName());
                    email.setText(yourModel.getEmail());
                    email.setEnabled(false);
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