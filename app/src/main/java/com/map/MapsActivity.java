package com.map;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    public static final ArrayList<String> places = new ArrayList<String>();
    String email;
    RelativeLayout mainrel;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleMap mMap;
    ImageView ThisLoc;
    EditText name, about;
    RatingBar rating, newrating;
    View layout, newlayout;
    LayoutInflater inflater;
    LayoutInflater newinflater;
    AlertDialog.Builder builder;
    AlertDialog.Builder newbuilder;
    private DatabaseReference mDatabase;
    private DatabaseReference mFindBase;
    boolean onCheckPressed = false;
    int rat=5;
    int num=1;
    boolean repeatPlace;
    ArrayList<String> Ids = new ArrayList<String>();
    static ArrayList<Place> Places = new ArrayList<Place>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ids.clear();
        mainrel = (RelativeLayout) findViewById(R.id.mainrel);

        repeatPlace = false;
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
//        inflater = getLayoutInflater();
//        layout = inflater.inflate(R.layout.new_place, null);
//        builder = new AlertDialog.Builder(this);
//        name = (EditText) layout.findViewById(R.id.name);
//        about = (EditText) layout.findViewById(R.id.about);
//        rating = (RatingBar) layout.findViewById(R.id.ratingBar);
//        newinflater = getLayoutInflater();
//        newlayout = newinflater.inflate(R.layout.new_mark, null);
//        newbuilder = new AlertDialog.Builder(this);
//        newrating = (RatingBar) newlayout.findViewById(R.id.newrating);
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
//        newbuilder.create();
//        builder.create();
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
        final long start = System.currentTimeMillis();
        for(int i=0; i<Places.size(); i++){
            final Place place = Places.get(i);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(place.lan, place.lat))
                    .title(place.name)
                    .snippet(place.about));

            Log.i("fbase", String.valueOf(place.lan+" "+place.lat));
            if(i==Places.size()-1){
                Toast.makeText(getApplicationContext(), "Карта готова!", Toast.LENGTH_SHORT).show();
            }
        }
        final long finish = System.currentTimeMillis();
        Log.i("fbase", finish-start+"");
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                newbuilder.setView(newlayout);
                for(int i=0; i<Places.size(); i++){
                    final Place place = Places.get(i);

                    String formattedDouble = new DecimalFormat("#0.00000").format(place.lat)+new DecimalFormat("#0.00000").format(place.lan);
                    double lan = marker.getPosition().latitude;
                    double lat = marker.getPosition().longitude;
                    final String latLng = new DecimalFormat("#0.00000").format(lat)+new DecimalFormat("#0.00000").format(lan);
                    Log.i("map", formattedDouble);
                    Log.i("map", latLng);
                    //String formattedDouble = new DecimalFormat("#0.00000").format(place.lat)+new DecimalFormat("#0.00000").format(place.lan);
                    final String Mail = email.replace('.', ',');
                    final String lli = formattedDouble.replace('.',',');

                    mFindBase = FirebaseDatabase.getInstance().getReference("users/"+Mail+"/");
                    mFindBase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                //Ids.add(dataSnapshot.child().getValue(String.class))
                                String value = singleSnapshot.getValue(String.class);
                                Log.i("testrepeat", value);
                                places.add(value);
//
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if(formattedDouble.equals(latLng)){
                        //final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        /////////////////////////////////////////////////////////////////
                        //String ratingf = new DecimalFormat("#0.0").format(place.rating);
                        Intent viewPlace = new Intent(MapsActivity.this, ViewPlace.class);
                        viewPlace.putExtra("num", i);
                        viewPlace.putExtra("lli", lli);
                        startActivity(viewPlace);
                        break;
                        //////////////////////////////////////////////////////////////////
//                        builder.setTitle(place.name)
//                                .setMessage(place.about+"\nРейтинг: "+ratingf)
//                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        //mainrel.removeView(builder.);
//                                        //mainrel.removeView(builder.);
//                                        dialog.cancel();
//                                    }
//                                })
//                                .setNegativeButton("Добавить свою оценку", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(final DialogInterface dialog, int which) {
//                                        if (user != null) {
//                                            for(int i=0; i<places.size(); i++){
//                                                //Log.i("testrepeat", places.get(i));
//                                                if(places.get(i).equals(lli)){
//                                                    Toast.makeText(getApplicationContext(), "Вы уже ставили оценку этой площадке!", Toast.LENGTH_SHORT).show();
//                                                    repeatPlace=true;
//                                                    break;
//                                                }
//                                            }
//                                            if(!repeatPlace){
//                                                newbuilder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        Log.i("newrat", newrating.getRating()+" "+place.rating);
//                                                        mDatabase.child("realese/"+ lli+"/rating").setValue((newrating.getRating()+place.rating)/2);
//                                                        mDatabase.child("users/"+Mail+"/"+lli).setValue(lli);
////                                                            mainrel.removeView(layout);
////                                                            mainrel.removeView(newlayout);
//                                                        //mainrel.removeView(newlayout);
//                                                        dialog.cancel();
////                                                        Intent i = new Intent(MapsActivity.this, MapsActivity.class);
////                                                        startActivity(i);
////                                                        finish();
//                                                    }
//                                                });
//                                                newbuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        mainrel.removeView(newlayout);
//                                                        dialog.cancel();
////                                                        Intent i = new Intent(MapsActivity.this, MapsActivity.class);
////                                                        startActivity(i);
////                                                        finish();
//                                                    }
//                                                });
//                                                newbuilder.show();
//                                            }
//                                            repeatPlace=false;
//                                        } else {
//                                            // User is signed out
//                                            Toast.makeText(getApplicationContext(), "Оценки может ставить только зарегестрированный пользователь. Войдите или зарегестрируйтесь в настройках", Toast.LENGTH_SHORT).show();
//                                            //dialog.cancel();
//                                        }
//                                        // ...
//                                    }
//                                });
//                        AlertDialog dialog = builder.create();
//                        dialog.show();
                    }
                }
                return false;
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(final LatLng latLng){
                if(onCheckPressed){
                    onCheckPressed=false;
                    ///////////////////////////////
                    Intent dialogNewPlace = new Intent(MapsActivity.this, DialogNewPlace.class);
                    dialogNewPlace.putExtra("lat",  latLng.latitude);
                    dialogNewPlace.putExtra("lan",  latLng.longitude);
                    Log.i("put", latLng.latitude+" "+latLng.longitude);
                    startActivity(dialogNewPlace);
                    ///////////////////////////////////
                }
            }
        });
    }

}