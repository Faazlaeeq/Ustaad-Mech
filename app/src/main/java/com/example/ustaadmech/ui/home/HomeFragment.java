package com.example.ustaadmech.ui.home;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.ustaadmech.R;
import android.content.ContentResolver;

import com.example.ustaadmech.SideActivityMech;
import com.example.ustaadmech.mechanic_data;
import com.example.ustaadmech.mechanicregister;
import com.example.ustaadmech.ui.dashboard.DashboardFragment;
import com.example.ustaadmech.ui.mechDisplay_frag;
import com.example.ustaadmech.ui.notifications.NotificationsFragment;
import com.example.ustaadmech.ui.profile_frag;
import com.example.ustaadmech.user_sign_up;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeFragment extends Fragment {
    CarouselView carouselView;
    BottomNavigationView bottomNavigationView;
    Button prof,mech;

    Button carrepair,bikerep;
    int[] sampleImages = {R.drawable.carousel11,R.drawable.carousel11};
    public HomeFragment(){
        // require a empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_home, container, false);
        carouselView = (CarouselView) v.findViewById(R.id.carouselView);


//                bottomNavigationView =(BottomNavigationView) inflater.inflate(R.layout.activity_sidemech, container, false).findViewById(R.id.nav_view_m);
//                bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);


        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);

        return v;

    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

}