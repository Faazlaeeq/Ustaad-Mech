package com.example.ustaadmech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ustaadmech.SendNotificationPack.APIService;
import com.example.ustaadmech.SendNotificationPack.Client;
import com.example.ustaadmech.SendNotificationPack.Data;
import com.example.ustaadmech.SendNotificationPack.MyResponse;
import com.example.ustaadmech.SendNotificationPack.NotificationSender;
import com.example.ustaadmech.SendNotificationPack.Token;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class adminapprove extends AppCompatActivity {
    FirebaseDatabase database;
    private String TAG="mainTag";
    RecyclerView re1;
    customadapter adapter;
    Button logout;
    private ArrayList<mechModel> list;
    private APIService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminapprove);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        re1 = findViewById(R.id.r1);
        re1.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<mechModel> options=new FirebaseRecyclerOptions.Builder<mechModel>()
                .setQuery(database.getInstance().getReference().child("MechanicsNonApproved").orderByChild("status").equalTo("0"),mechModel.class).build();

        adapter=new customadapter(options,this);


        re1.setAdapter(adapter);

        logout=findViewById(R.id.aa_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                reload();
            }
        });

        UpdateToken();

    }

    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseMessaging.getInstance().getToken().toString();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }


    void reload()
    {
        Intent intent =new Intent(adminapprove.this,MainActivity.class);
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