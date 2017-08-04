package com.map;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    FirebaseUser user;
    String email;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleMap mMap;
    ImageView ThisLoc;
    EditText name, about;
    RatingBar rating;
    View layout;
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    private DatabaseReference mDatabase;
    boolean onCheckPressed = false;
    int num=1;
    ArrayList<Place> Places = new ArrayList<Place>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Places = Main.places;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ThisLoc = (ImageView) findViewById(R.id.plus);
        ThisLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Выберите место на карте!", Toast.LENGTH_SHORT).show();
                onCheckPressed=true;
            }
        });
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.new_place, null);
        builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        name = (EditText) layout.findViewById(R.id.name);
        about = (EditText) layout.findViewById(R.id.about);
        rating = (RatingBar) layout.findViewById(R.id.ratingBar);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i("fbase", String.valueOf(Places.size()));
        for(int i=0; i<Places.size(); i++){
            Place place = Places.get(i);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(place.lan, place.lat))
                    .title(place.name));
            Log.i("fbase", String.valueOf(place.lan+" "+place.lat));
            if(i==Places.size()-1){
                Toast.makeText(getApplicationContext(), "Карта готова!", Toast.LENGTH_SHORT).show();
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(final LatLng latLng){
                if(onCheckPressed){
                    onCheckPressed=false;
                    builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Place place = new Place(name.getText().toString(), about.getText().toString(), rating.getNumStars(), latLng.longitude, latLng.latitude);
                            String formattedDouble = new DecimalFormat("#0.00000").format(place.lat)+new DecimalFormat("#0.00000").format(place.lan);
                            String Mail = email.replace('.', ',');
                            String lli = formattedDouble.replace('.',',');
                            mDatabase.child("test/"+ lli).setValue(place);
                            if(Mail!="noname")
                                mDatabase.child("users/"+Mail+"/"+lli).setValue(rating.getNumStars());
                            Toast.makeText(getApplicationContext(), "Спасибо! Мы рассмотрим ваше предложение.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create();
                    builder.show();
//                    final EditText edittext = new EditText(getApplicationContext());
//                    edittext.setTextColor(Color.BLACK);
//                    final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
//                    alert.setMessage("Введите информативное название");
//                    alert.setTitle("Новая площадка");
//                    alert.setView(edittext);
//                    alert.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            if(edittext.getText().toString()!=null){
//                                Place place = new Place(edittext.getText().toString(), latLng.longitude, latLng.latitude);
//                                String formattedDouble = new DecimalFormat("#0.00000").format(place.lat)+new DecimalFormat("#0.00000").format(place.lan);
//                                String ll = place.lat+","+place.lan;
//                                String lli = formattedDouble.replace('.',',');
//                                mDatabase.child("test/"+ lli).setValue(place);
//                                Toast.makeText(getApplicationContext(), "Спасибо! Мы рассмотрим ваше предложение.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                    alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                        }
//                    });
//                    alert.show();
                }
            }
        });
    }
//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();  // optional depending on your needs
//        Intent i = new Intent(MapsActivity.this, Main.class);
//        startActivity(i);
//        overridePendingTransition(0,0);
//    }
}
