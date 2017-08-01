package com.map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Main extends Activity {
    TextView Workout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("realese");
    public static final ArrayList<Place> places = new ArrayList<Place>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Place place = singleSnapshot.getValue(Place.class);
                    places.add(place);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Workout = (TextView) findViewById(R.id.work);
        Workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main.this, MapsActivity.class);
                //i.putArrayListExtra("list", places);
                startActivity(i);
            }
        });
    }
    public void pools(View view){
        String geoUriString = "geo:0,0?q=бассейны рядом со мной&z=4";
        Uri geoUri = Uri.parse(geoUriString);
        Intent map = new Intent(Intent.ACTION_VIEW, geoUri);
        startActivity(map);
    }

}
