package com.example.ustaadmech.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ustaadmech.R;
import com.example.ustaadmech.hireModel;
import com.example.ustaadmech.m_d_customadapter;
import com.example.ustaadmech.mechModel;
import com.example.ustaadmech.ordercustomadapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class orderDisplay_frag extends Fragment {


    public orderDisplay_frag() {
        // Required empty public constructor
    }


    RecyclerView re1;
    ordercustomadapter adapter;
    EditText ssss;
    String a;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_order_display_frag, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        re1 = v.findViewById(R.id.r5);
        re1.setLayoutManager(new LinearLayoutManager(v.getContext()));

        FirebaseRecyclerOptions<hireModel> options=new FirebaseRecyclerOptions.Builder<hireModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("orders").orderByChild("status_email").equalTo(user.getEmail()+"_requested"),hireModel.class).build();


        adapter=new ordercustomadapter(options,v.getContext());


        re1.setAdapter(adapter);
        return v;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
//        ssss = (EditText) getActivity().findViewById(R.id.abcd); // inititate a search view
//        ssss.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(charSequence.length()!=0){
//                    a=ssss.getText().toString();
//                    processsearch(a);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    private void processsearch(String s){

        FirebaseRecyclerOptions<mechModel> options=new FirebaseRecyclerOptions.Builder<mechModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Mechanics").orderByChild("name").startAt(s).endAt(s+"\uf8ff"),mechModel.class).build();

        adapter=new ordercustomadapter(options,getView().getContext());
        adapter.startListening();

        re1.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();

        adapter.startListening();
        re1.setAdapter(adapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}