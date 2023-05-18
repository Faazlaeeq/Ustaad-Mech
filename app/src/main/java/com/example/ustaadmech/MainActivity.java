package com.example.ustaadmech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button login,signup,mechReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.log);
        signup=findViewById(R.id.sign);
        mechReg=findViewById(R.id.mechReg);

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(MainActivity.this,log_in.class);
                        startActivity(intent);
                    }
                }
        );
        signup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(MainActivity.this,sign_up.class);
                        startActivity(intent);
                    }
                }
        );
        mechReg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(MainActivity.this,mechanicregister.class);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            reload();
        }
    }
    void reload()
    {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("users");
        String userEmail=fUser.getEmail();
        postRef.orderByChild("email").equalTo(userEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (fUser.getEmail().equals("admin@gmail.com"))
                        {
                            Intent intent=new Intent(MainActivity.this,adminapprove.class);
                            startActivity(intent);
                        }
                        else if(dataSnapshot.exists()){
                            Intent newintent=new Intent(MainActivity.this,SideActivity.class);
                            newintent.putExtra("email",fUser.getEmail());
                            newintent.putExtra("id",fUser.getUid());
                            newintent.putExtra("name",fUser.getDisplayName());
                            startActivity(newintent);
                        }
                        else {

                            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Mechanics");
                            String userEmail=fUser.getEmail();
                            postRef.orderByChild("email").equalTo(userEmail).addValueEventListener(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){

                                                Intent newintent=new Intent(MainActivity.this,SideActivityMech.class);
                                                newintent.putExtra("email",fUser.getEmail());
                                                newintent.putExtra("id",fUser.getUid());
                                                newintent.putExtra("name",fUser.getDisplayName());
                                                startActivity(newintent);
                                            }
                                            else {
                                                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("MechanicsNonApproved");
                                                String userEmail=fUser.getEmail();
                                                postRef.orderByChild("email").equalTo(userEmail).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                                        if(snapshot2.exists()){

                                                            Intent newintent=new Intent(MainActivity.this,doneRegister.class);
                                                            newintent.putExtra("email",fUser.getEmail());
                                                            newintent.putExtra("id",fUser.getUid());
                                                            newintent.putExtra("name",fUser.getDisplayName());
                                                            startActivity(newintent);
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    }
                            );





                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}