package com.example.ustaadmech;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class feedbacks extends AppCompatActivity {
    FirebaseDatabase database;
    private String TAG="mainTag";
    RecyclerView re1;
    customadapter adapter;
    private ArrayList<mechModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminapprove);

        re1 = findViewById(R.id.r1);
        re1.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<mechModel> options=new FirebaseRecyclerOptions.Builder<mechModel>()
                .setQuery(database.getInstance().getReference().child("Mechanics").orderByChild("status").equalTo("0"),mechModel.class).build();

        adapter=new customadapter(options,this);


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