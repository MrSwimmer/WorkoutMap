package com.map;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class DialogNewPlace extends AppCompatActivity {
    EditText name, about;
    RatingBar rating;
    Button save;
    String lat;
    String lan;
    double Lat, Lan, m=1;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    String email;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_place);
        name = (EditText) findViewById(R.id.name);
        about = (EditText) findViewById(R.id.about);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        save = (Button) findViewById(R.id.save);
        Lat = intent.getDoubleExtra("lat", m);
        Lan = intent.getDoubleExtra("lan", m);
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Place place = new Place(name.getText().toString(), about.getText().toString(), rating.getRating(), Lan, Lat);
                String formattedDouble = new DecimalFormat("#0.00000").format(place.lat)+new DecimalFormat("#0.00000").format(place.lan);
                String Mail = email.replace('.', ',');
                String lli = formattedDouble.replace('.',',');
                mDatabase.child("test/"+ lli).setValue(place);
                if(Mail!="noname")
                    mDatabase.child("users/"+Mail+"/"+lli).setValue(lli);
                Toast.makeText(getApplicationContext(), "Спасибо! Мы рассмотрим ваше предложение.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
