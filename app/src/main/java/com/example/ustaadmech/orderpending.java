package com.example.ustaadmech;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class orderpending extends AppCompatActivity {
    FirebaseDatabase database;
    private String TAG="mainTag";
    RecyclerView re1;
    order_p_customadapter adapter;
    Button logout;
    private ArrayList<mechModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pending);

        re1 = findViewById(R.id.r1);
        re1.setLayoutManager(new LinearLayoutManager(this));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseRecyclerOptions<hireModel> options=new FirebaseRecyclerOptions.Builder<hireModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("orders").orderByChild("status_email").equalTo(user.getEmail()+"_coming"),hireModel.class).build();

        adapter=new order_p_customadapter(options,this);


        re1.setAdapter(adapter);



    }

    @Override
    public void onBackPressed() {

    }

    void reload()
    {
        Intent intent =new Intent(orderpending.this,MainActivity.class);
        startActivity(intent);

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