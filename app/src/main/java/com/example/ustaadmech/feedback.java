package com.example.ustaadmech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class feedback extends AppCompatActivity {
    FirebaseDatabase database;
    private String TAG="mainTag";
    RecyclerView re1;
    feebackadap adapter;
    private FirebaseAuth mAuth;

    private ArrayList<mechModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mAuth = FirebaseAuth.getInstance();

        re1 = findViewById(R.id.r3);
        re1.setLayoutManager(new LinearLayoutManager(this));
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        FirebaseRecyclerOptions<feedbackmodel> options=new FirebaseRecyclerOptions.Builder<feedbackmodel>()
                .setQuery(database.getInstance().getReference().child("feedback").orderByChild("mech_email").equalTo(firebaseUser.getEmail()),feedbackmodel.class).build();

        adapter=new feebackadap(options,this);


        re1.setAdapter(adapter);


    }
    @Override
    protected void onStart()
    {


        adapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        adapter.stopListening();
        super.onStop();
    }

}