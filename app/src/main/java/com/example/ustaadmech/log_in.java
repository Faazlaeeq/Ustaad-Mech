package com.example.ustaadmech;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustaadmech.SendNotificationPack.updatetoken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class log_in extends AppCompatActivity {

    private SignInButton signInButton;
    private GoogleSignInClient googleSignInClient;
    private String TAG="mainTag";
    private FirebaseAuth mAuth;

    private Button btnSignOut;
    EditText name,email,pass;
    FirebaseDatabase database;
    Button sign_up;
    private int RESULT_CODE_SINGIN=999;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_in);




        database = FirebaseDatabase.getInstance();
        pass = findViewById(R.id.editTextPassword);
        email = findViewById(R.id.editTextTextEmail);
        //initialization
        signInButton = findViewById(R.id.SignIn_Button);
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Sign in with Google");
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        //Attach a onClickListener
        signInButton.setOnClickListener(v -> signInM());
    }
    private void signInM() {
        Intent singInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(singInIntent,RESULT_CODE_SINGIN);
    }

    // onActivityResult (Here we handle the result of the Activity )
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE_SINGIN) {
            //just to verify the code
            //create a Task object and use GoogleSignInAccount from Intent and write a separate method to handle singIn Result.

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        //we use try catch block because of Exception.
        try {
            signInButton.setVisibility(View.INVISIBLE);
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(log_in.this,"Signed In successfully",Toast.LENGTH_LONG).show();
            //SignIn successful now show authentication
            FirebaseGoogleAuth(account);

        } catch (ApiException e) {
            e.printStackTrace();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(log_in.this,"SignIn Failed!!!",Toast.LENGTH_LONG).show();
//            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        //here we are checking the Authentication Credential and checking the task is successful or not and display the message
        //based on that.
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                Toast.makeText(log_in.this,"successful",Toast.LENGTH_LONG).show();
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                UpdateUI(firebaseUser);
            }
            else {
                Toast.makeText(log_in.this,"Failed!",Toast.LENGTH_LONG).show();
                UpdateUI(null);
            }
        });
    }
    public void login(View view) {
        String em= email.getText().toString();
        String ps= pass.getText().toString();
        final String Email=email.getText().toString();
        final String Pass=pass.getText().toString();

        if(!Email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
        {
            email.requestFocus();
            email.setError("Please Enter Your Valid Email Address");
        }

        else if(Pass.length()<=5)
        {
            pass.requestFocus();
            pass.setError("Password Must Be More Than 5 Digits");
        }

        else
        {

            mAuth.signInWithEmailAndPassword(em,ps)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (em.equals("admin@gmail.com"))
                                {
                                    updatetoken ut=new updatetoken();
                                    ut.UpdateToken(em);
                                    Intent intentnew=new Intent(log_in.this,adminapprove.class);
                                    startActivity(intentnew);
                                }
                                else {
                                    Toast.makeText(log_in.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                                    UpdateUI(user);}

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(log_in.this);
                                builder.setMessage("Incorrect Email Or Password!");
                                builder.setPositiveButton("Try Again", (dialogInterface, i) -> {
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.dismiss();
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                UpdateUI(null);
                            }
                        }
                    });
        }

    }
    private void UpdateUI(FirebaseUser fUser) {

        //getLastSignedInAccount returned the account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (account !=null|| fUser!=null){
            updatetoken ut=new updatetoken();
            ut.UpdateToken(fUser.getEmail());
            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("users");
            String userEmail=fUser.getEmail();
            postRef.orderByChild("email").equalTo(userEmail)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Intent newintent=new Intent(log_in.this,SideActivity.class);
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

                                                    Intent newintent=new Intent(log_in.this,SideActivityMech.class);
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

                                                                Intent newintent=new Intent(log_in.this,doneRegister.class);
                                                                newintent.putExtra("email",fUser.getEmail());
                                                                newintent.putExtra("id",fUser.getUid());
                                                                newintent.putExtra("name",fUser.getDisplayName());
                                                                startActivity(newintent);
                                                            }
                                                            else {
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(log_in.this);
                                                                builder.setMessage("Incorrect Email Or Password!");
                                                                builder.setPositiveButton("Try Again", (dialogInterface, i) -> {
                                                                    AlertDialog alertDialog = builder.create();
                                                                    alertDialog.dismiss();
                                                                });
                                                                AlertDialog alertDialog = builder.create();
                                                                alertDialog.show();
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


//            String personName = account.getDisplayName();
//            String personGivenName = account.getGivenName();
//            String personEmail = account.getEmail();
//            String personId = account.getId();
//
//            Toast.makeText(log_in.this,personName + "  " + personEmail,Toast.LENGTH_LONG).show();
        }
    }





}