package com.example.ustaadmech;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.ustaadmech.databinding.ActivityMainBinding;
import com.example.ustaadmech.ui.dashboard.DashboardFragment;
import com.example.ustaadmech.ui.home.HomeFragment;
import com.example.ustaadmech.ui.mechDisplay_frag;
import com.example.ustaadmech.ui.mechprofile_frag;
import com.example.ustaadmech.ui.notifications.NotificationsFragment;
import com.example.ustaadmech.ui.orderDisplay_frag;
import com.example.ustaadmech.ui.profile_frag;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SideActivityMech extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    ActivityMainBinding binding;
    DrawerLayout drw;
    TextView nav_h_email;
    ActionBarDrawerToggle toggle;
    NavigationView navbar;
    Toolbar toolbar1;
    private GoogleSignInClient googleSignInClient;

    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment= new HomeFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidemech);

        drw = findViewById(R.id.drw_m);
        toolbar1 = findViewById(R.id.toolbar1_m);
        navbar = findViewById(R.id.navview_m);
        setSupportActionBar(toolbar1);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_m, homeFragment).commit();



        bottomNavigationView = findViewById(R.id.nav_view);
//
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);


        NavigationView navigationView = (NavigationView) findViewById(R.id.navview_m);
        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = (TextView) headerView.findViewById(R.id.nav_h_email);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_h_name);
        ImageView profileimg=(ImageView)headerView.findViewById(R.id.profileimgside);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        navEmail.setText(user.getEmail());
        navEmail.setText(user.getEmail());


        Query query = FirebaseDatabase.getInstance().getReference("Mechanics").orderByChild("email").equalTo(user.getEmail());
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
                Toast.makeText(SideActivityMech.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        toggle = new ActionBarDrawerToggle(this,drw,toolbar1,R.string.open,R.string.close);
        drw.addDrawerListener(toggle);
        toggle.syncState();
        orderDisplay_frag user_mech_display = new orderDisplay_frag();
        navbar.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drw.closeDrawer( GravityCompat.START);
                switch (item.getItemId()){
                    case R.id.nav_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_m, profile).commit();

                        break;

                    case R.id.nav_feedback:
                        Intent intent=new Intent(SideActivityMech.this,feedback.class);
                        startActivity(intent);

                        break;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build();
                        googleSignInClient = GoogleSignIn.getClient(SideActivityMech.this,gso);

                        googleSignInClient.signOut();

                        reload();
                        break;


                };

                return true;
            }
        } );

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
        Intent intent =new Intent(SideActivityMech.this,MainActivity.class);
        startActivity(intent);

    }



    DashboardFragment firstFragment = new DashboardFragment();
    mechprofile_frag profile = new mechprofile_frag();
    HomeFragment home = new HomeFragment();
    orderDisplay_frag dashboardFragment = new orderDisplay_frag();
    NotificationsFragment notificationsFragment= new NotificationsFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_m, home).commit();
                return true;

            case R.id.navigation_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_m, dashboardFragment).commit();
                return true;


            case R.id.navigation_notifications:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_m, profile).commit();
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