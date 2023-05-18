package com.example.ustaadmech;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ustaadmech.databinding.ActivityMainBinding;
import com.example.ustaadmech.ui.dashboard.DashboardFragment;
import com.example.ustaadmech.ui.home.HomeFragment;
import com.example.ustaadmech.ui.mechDisplay_frag;
import com.example.ustaadmech.ui.notifications.NotificationsFragment;
import com.example.ustaadmech.ui.profile_frag;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SideActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    ActivityMainBinding binding;
    DrawerLayout drw;
    TextView nav_h_email;
    ActionBarDrawerToggle toggle;
    FusedLocationProviderClient client;

    NavigationView navbar;
    private GoogleSignInClient googleSignInClient;
double longi;
double lati;
    Toolbar toolbar1;
    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment= new HomeFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side);

        drw = findViewById(R.id.drw);
        toolbar1 = findViewById(R.id.toolbar1);
        navbar = findViewById(R.id.navview);
        setSupportActionBar(toolbar1);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();



        bottomNavigationView = findViewById(R.id.nav_view);
//
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);


        NavigationView navigationView = (NavigationView) findViewById(R.id.navview);
        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = (TextView) headerView.findViewById(R.id.nav_h_email);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_h_name);
        ImageView profileimg=(ImageView)headerView.findViewById(R.id.profileimgside);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        navEmail.setText(user.getEmail());
        navEmail.setText(user.getEmail());


        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    user_sign_up yourModel = childSnapshot.getValue(user_sign_up.class);

                    navUsername.setText(yourModel.getName());
                    Glide.with(headerView).
                            load(yourModel.getImgurl()).
                            into(profileimg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SideActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        toggle = new ActionBarDrawerToggle(this,drw,toolbar1,R.string.open,R.string.close);
        drw.addDrawerListener(toggle);
        toggle.syncState();
        mechDisplay_frag user_mech_display = new mechDisplay_frag();
        navbar.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drw.closeDrawer( GravityCompat.START);
                switch (item.getItemId()){
                    case R.id.nav_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profile).commit();

                        break;
                    case R.id.nav_feedback:
                        Intent feedintent=new Intent(SideActivity.this,feedback.class);
                        startActivity(feedintent);
                        break;
                    case R.id.nav_joinmech:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, user_mech_display).commit();

                        break;
                    case R.id.nav_getlocationnow:
                        client = LocationServices.getFusedLocationProviderClient(SideActivity.this);
                        if (ActivityCompat.checkSelfPermission(SideActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            getCurrentLocation();
                        } else {
                            ActivityCompat.requestPermissions(SideActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                        };

                        break;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build();
                        googleSignInClient = GoogleSignIn.getClient(SideActivity.this,gso);

                        googleSignInClient.signOut();
                        //here

                        reload();
                        break;


                };

                return true;
            }
        } );

    }

    public void OnRequestPermissionsResult(int requestcode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (requestcode == 44) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }

    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    longi = location.getLongitude();
                    lati = location.getLatitude();

                    final String[] userId = new String[1];
                    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

                    FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(fUser.getEmail()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot childSnapshot: snapshot.getChildren()) {
                                 userId[0] =childSnapshot.getKey();
                                FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(fUser.getEmail());
                                Map<String, Object> map = new HashMap<>();

                                map.put("longi", longi);
                                map.put("lati", lati);
                                map.put("goehash", lati+","+longi);
                                childSnapshot.getRef().updateChildren(map);

                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    Geocoder geocoder = new Geocoder(SideActivity.this, Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(lati, longi, 1);
                        Log.d("addresss",addresses.toString());


                        Toast.makeText(SideActivity.this, "Location Set", Toast.LENGTH_SHORT).show();

                        //getting address-----------------------------
                        //country
                        //state
//                        ShowAddress.setText(addresses.get(0).getAdminArea().toString()+"\n");
                        //city
//                          ShowAddress.setText(addresses.get(0).getLocality().toString()+"\n");
                        //area
                        //   ShowAddress.setText(addresses.get(0).getSubLocality().toString()+"\n")
                        // block
                        // ShowAddress.setText(addresses.get(0).getFeatureName().toString()+"\n");
                        //Adress
//                        ShowAddress.setText(addresses.get(0).getAddressLine(0).toString()+"\n");


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            reload();
        }
    }
    void reload()
    {
        Intent intent =new Intent(SideActivity.this,MainActivity.class);
        startActivity(intent);

    }



    DashboardFragment firstFragment = new DashboardFragment();
    profile_frag profile = new profile_frag();
    HomeFragment home = new HomeFragment();
    mechDisplay_frag dashboardFragment = new mechDisplay_frag();
    NotificationsFragment notificationsFragment= new NotificationsFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
                return true;

            case R.id.navigation_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, dashboardFragment).commit();
                return true;


            case R.id.navigation_notifications:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profile).commit();
                return true;
        }
        return false;
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

}