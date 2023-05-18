package com.example.ustaadmech.SendNotificationPack;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class updatetoken {
    public void UpdateToken(String uid){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final String[] refreshToken = new String[1];
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                refreshToken[0] = task.getResult();
                Token token= new Token(refreshToken[0]);
                FirebaseDatabase.getInstance().getReference("Tokens").child(uid.replace(".com","")).setValue(token);
            }
        });

    }
}
