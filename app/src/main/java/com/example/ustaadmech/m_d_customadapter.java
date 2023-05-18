package com.example.ustaadmech;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class m_d_customadapter extends FirebaseRecyclerAdapter <mechModel, m_d_myvieholder> {
    Context context;
    double logi,lati;
    float radius;
    public m_d_customadapter(@NonNull FirebaseRecyclerOptions options, Context context,double logi,double lati,float radius)
    {
        super(options);
        this.context=context;
        this.logi=logi;
        this.lati=lati;
        this.radius=radius;
    }

    @Override
    protected void onBindViewHolder(@NonNull m_d_myvieholder holder, int position, @NonNull mechModel model1)
    {




        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("location");
        GeoFire geoFire = new GeoFire(ref);

        final double[] userlogi = new double[1];
        final double[] userlati = new double[1];

        final int[] loop = {0};
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lati,logi),radius);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (model1.getEmail().equals(key+".com"))
                {
                    holder.card.setVisibility(View.VISIBLE);

                    holder.name.setText(model1.getName());
                    holder.address.setText(model1.getAddress());

                    if (model1.getRating()==null||model1.getRating()=="")
                    {
                        holder.rating.setText("N/A");

                    }
                    else{
                        holder.rating.setText(model1.getRating());}

                    Glide.with(context).
                            load(model1.getImgurl()).
                            into(holder.img);

                    holder.card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context,mech_info.class);
                            intent.putExtra("mechEmail",model1.getEmail());
                            intent.putExtra("orderid",  getRef(position).getKey());
                            context.startActivity(intent);
                        }
                    });
                    loop[0]++;

                }
                else if( loop[0] ==0) {
                    holder.card.setVisibility(View.GONE);

                }



                Log.d(TAG, "onKeyEntered: "+String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onKeyExited(String key) {
                Log.d(TAG, "onKeyExited: "+String.format("Key %s is no longer in the search area", key));
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d(TAG, "onKeyMoved: "+ String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                Log.d(TAG, "onGeoQueryReady: All initial data has been loaded and events have been fired!");
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.d(TAG, "There was an error with this query: " + error);
                System.err.println("There was an error with this query: " + error);
            }
        });









    }

    @NonNull
    @Override
    public m_d_myvieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate( R.layout.r_l_mechdis, parent,false);
        return new m_d_myvieholder(view);

    }

    @Override
    public int getItemCount()
    {
//        int id=super.getItemCount();
        return super.getItemCount();
    }

}


class m_d_myvieholder extends RecyclerView.ViewHolder{

    TextView name,rating,address;
    LinearLayout layout;
ImageView img;
CardView card;
    public m_d_myvieholder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.m_d_mech_name);
        rating=itemView.findViewById(R.id.m_d_rating);
        img=itemView.findViewById(R.id.m_d_mechImg);
        card=itemView.findViewById(R.id.m_d_card);
        layout=itemView.findViewById(R.id.r_l_layout);
        address=itemView.findViewById(R.id.m_l_address);

    }
}
