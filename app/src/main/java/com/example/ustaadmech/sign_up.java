package com.example.ustaadmech;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustaadmech.SendNotificationPack.updatetoken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity {

    private SignInButton signInButton;
    private GoogleSignInClient googleSignInClient;
    private String TAG="mainTag";
    private FirebaseAuth mAuth;
    user_sign_up m=new user_sign_up();
    private Button btnSignOut;
    EditText name,email,pass,repass;
    FirebaseDatabase database;
    Button sign_up;
    private int RESULT_CODE_SINGIN=999;
    private CallbackManager mCallbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        name=findViewById(R.id.editTextTextPersonName);
        email=findViewById(R.id.editTextTextEmailAddress);
        pass=findViewById(R.id.editTextTextPassword);
        repass=findViewById(R.id.editTextTextPassword2);
        database = FirebaseDatabase.getInstance();
        sign_up=findViewById(R.id.sign_up);
        user_sign_up m= new user_sign_up();
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Name = name.getText().toString();
                final String Email = email.getText().toString();
                final String Pass = pass.getText().toString();

                if (Name.length() == 0) {
                    name.requestFocus();
                    name.setError("Please Enter Your Name");
                } else if (!Name.matches("[a-zA-Z ]+")) {
                    name.requestFocus();
                    name.setError("Please Enter Only Alphabetic Character");
                } else if (!Email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    email.requestFocus();
                    email.setError("Please Enter Your Valid Email Address");
                } else if (Pass.length() <= 5) {
                    pass.requestFocus();
                    pass.setError("Password Must Be More Than 5 Digits");
                } else if (!repass.getText().toString().equals(pass.getText().toString()) ) {
                    pass.requestFocus();
                    pass.setError("Both passwords don't match!");
                }
                else {

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (task.isSuccessful()) {
                                    m.setName(name.getText().toString());
                                    m.setEmail(email.getText().toString());
                                    m.setPass(pass.getText().toString());


                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("users").child(id).setValue(m);
                                    updatetoken ut=new updatetoken();
                                    ut.UpdateToken(email.getText().toString());

                                    FirebaseUser user = mAuth.getCurrentUser();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(sign_up.this);
                                    builder.setMessage("Thank you for the registration!");
                                    builder.setPositiveButton("Let's Explore", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent=new Intent(sign_up.this,SideActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }
                        });
            }









            }
        });

        //initialization
        signInButton = findViewById(R.id.SignIn_Button);
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Sign in with Google");
        mAuth = FirebaseAuth.getInstance();
//        btnSignOut.setVisibility(View.INVISIBLE);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are         included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

//         Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this,gso);

        //Attach a onClickListener
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInM();
            }
        });

//        // [START initialize_fblogin]
//        // Initialize Facebook Login button
//        mCallbackManager = CallbackManager.Factory.create();
//        LoginButton loginButton = findViewById(R.id.fb);
//        loginButton.setReadPermissions("email", "public_profile");
//        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d(TAG, "facebook:onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d(TAG, "facebook:onError", error);
//            }
//        });
//        // [END initialize_fblogin]





    }
    // [START auth_with_facebook]
//    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Toast.makeText(sign_up.this,"chl gya",Toast.LENGTH_LONG).show();
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(sign_up.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
//    }
    // [END auth_with_facebook]
    //when the signIn Button is clicked then start the signIn Intent
    private void signInM() {
        Intent singInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(singInIntent,RESULT_CODE_SINGIN);
    }

    // onActivityResult (Here we handle the result of the Activity )
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE_SINGIN) {        //just to verify the code
            //create a Task object and use GoogleSignInAccount from Intent and write a separate method to handle singIn Result.

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        //we use try catch block because of Exception.
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(sign_up.this,"Signed In successfully",Toast.LENGTH_LONG).show();



            //SignIn successful now show authentication
            FirebaseGoogleAuth(account);



        } catch (ApiException e) {
            e.printStackTrace();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(sign_up.this,"SignIn Failed!!!",Toast.LENGTH_LONG).show();
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        //here we are checking the Authentication Credential and checking the task is successful or not and display the message
        //based on that.
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(sign_up.this,"successful",Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    UpdateUI(firebaseUser);
                }
                else {
                    Toast.makeText(sign_up.this,"Failed!",Toast.LENGTH_LONG).show();
                    UpdateUI(null);
                }
            }
        });
    }

    private void UpdateUI(FirebaseUser fUser) {

        //getLastSignedInAccount returned the account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account !=null){
            m.setEmail(fUser.getEmail());
            m.setName(fUser.getDisplayName());
            m.setImgurl(fUser.getPhotoUrl().toString());
            m.setPhone(fUser.getPhoneNumber());
            FirebaseDatabase.getInstance().getReference("users").push().setValue(m);
            updatetoken ut=new updatetoken();
            ut.UpdateToken(fUser.getEmail());
            Intent intent=new Intent(sign_up.this,SideActivity.class);
            intent.putExtra("google",account);
            intent.putExtra("emailandpass",fUser);
            startActivity(intent);
//            String personName = account.getDisplayName();
//            String personGivenName = account.getGivenName();
//            String personEmail = account.getEmail();
//            String personId = account.getId();
//
//            Toast.makeText(log_in.this,personName + "  " + personEmail,Toast.LENGTH_LONG).show();
        }
    }

}