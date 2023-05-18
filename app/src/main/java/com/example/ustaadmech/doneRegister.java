package com.example.ustaadmech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class doneRegister extends AppCompatActivity {
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_register);
        logout=findViewById(R.id.d_r_logout);
        logout.setText("Log Out");
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                reload();
            }
        });
    }
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
    void reload()
    {
        Intent intent =new Intent(doneRegister.this,MainActivity.class);
        startActivity(intent);

    }
}
