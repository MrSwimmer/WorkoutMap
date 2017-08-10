package com.map;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewPlace extends AppCompatActivity {
    TextView name, about;
    Button ok, add;
    RatingBar rating;
    int num;
    boolean havemark;
    public static String lli;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    public static String email;
    public static Place place;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        havemark=false;
        num = intent.getIntExtra("num", 0);
        lli = intent.getStringExtra("lli");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_place);
        name = (TextView) findViewById(R.id.nameplace);
        about = (TextView) findViewById(R.id.aboutplace);
        ok = (Button) findViewById(R.id.ok);
        add = (Button) findViewById(R.id.addmark);
        rating = (RatingBar) findViewById(R.id.ratingview);
        place = MapsActivity.Places.get(num);
        name.setText(place.name);
        rating.setRating(place.rating);
        about.setText(place.about);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    email = user.getEmail();
                    Log.i("LOG", email);
                    // User is signed in
                } else {
                    email = "noname";
                    // User is signed out
                }
            }
        };
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    for (int i = 0; i < MapsActivity.places.size(); i++) {
                        //Log.i("testrepeat", places.get(i));
                        if (MapsActivity.places.get(i).equals(lli)) {
                            Toast.makeText(getApplicationContext(), "Вы уже ставили оценку этой площадке!", Toast.LENGTH_SHORT).show();
                            havemark=true;
                            break;
                        }
                    }
                    if(!havemark){
                        Intent i = new Intent(ViewPlace.this, AddMark.class);
                        startActivity(i);
                    }
                }
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
