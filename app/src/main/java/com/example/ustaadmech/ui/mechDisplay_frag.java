package com.example.ustaadmech.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ustaadmech.R;
import com.example.ustaadmech.m_d_customadapter;
import com.example.ustaadmech.mechModel;
import com.example.ustaadmech.user_sign_up;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class mechDisplay_frag extends Fragment {


    public mechDisplay_frag() {
        // Required empty public constructor
    }


    RecyclerView re1;
    m_d_customadapter adapter;
    EditText ssss;
    String a;
    RadioButton name,area;
    EditText radius;
    float radi;
    double a_longi,a_lati;
    String i_car,i_bike;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        String  userEmail=fUser.getEmail();
        Query query2 = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(userEmail);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot: snapshot.getChildren()) {
                    user_sign_up yourModel = childSnapshot.getValue(user_sign_up.class);
                    if (yourModel.getLongi()!=null&&yourModel.getLati()!=null) {
                        a_lati = yourModel.getLati();
                        a_longi = yourModel.getLongi();
                    }
                    else {

                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setTitle("Error");
                        builder.setMessage("Please set your Location from menu in left panel.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    FirebaseRecyclerOptions<mechModel> options;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_mech_display_frag, container, false);

        re1 = v.findViewById(R.id.r2);
        re1.setLayoutManager(new LinearLayoutManager(v.getContext()));

        name=v.findViewById(R.id.radioButtonname);
        area=v.findViewById(R.id.radioButtonarea);
        ssss=v.findViewById(R.id.searchtext);
        radius =v.findViewById(R.id.m_d_radius);
        radi=Float.parseFloat(radius.getText().toString());







        options=new FirebaseRecyclerOptions.Builder<mechModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Mechanics"),mechModel.class).build();



        adapter=new m_d_customadapter(options,v.getContext(),a_longi,a_lati, radi);



        re1.setAdapter(adapter);

//        adapter.startListening();



        ssss.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                options=new FirebaseRecyclerOptions.Builder<mechModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Mechanics"),mechModel.class).build();
                adapter=new m_d_customadapter(options,v.getContext(),a_longi,a_lati, radi);


                re1.setAdapter(adapter);
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    options=new FirebaseRecyclerOptions.Builder<mechModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Mechanics").orderByChild("name").startAt(charSequence.toString()).endAt(charSequence.toString()+"\uf8ff"),mechModel.class).build();
                    adapter=new m_d_customadapter(options,v.getContext(),a_longi,a_lati, radi);

                    adapter.startListening();

                    re1.setAdapter(adapter);



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        radius.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty())
                {

                options = new FirebaseRecyclerOptions.Builder<mechModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Mechanics"), mechModel.class).build();
                adapter = new m_d_customadapter(options, v.getContext(), a_longi, a_lati, Float.parseFloat(charSequence.toString()));


                re1.setAdapter(adapter);
            }

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty())
                {

                    options=new FirebaseRecyclerOptions.Builder<mechModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Mechanics").orderByChild("name").startAt(ssss.getText().toString()).endAt(ssss.getText().toString()+"\uf8ff"),mechModel.class).build();
                    adapter=new m_d_customadapter(options,v.getContext(),a_longi,a_lati, Float.parseFloat(charSequence.toString()));

                    adapter.startListening();

                    re1.setAdapter(adapter);

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return v;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
//        options=new FirebaseRecyclerOptions.Builder<mechModel>()
//                .setQuery(FirebaseDatabase.getInstance().getReference().child("Mechanics"),mechModel.class).build();
//
//        adapter=new m_d_customadapter(options,view.getContext(),a_longi,a_lati);
//
//
//        re1.setAdapter(adapter);

    }


    @Override
    public void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}