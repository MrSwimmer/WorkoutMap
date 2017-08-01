package com.map;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ImageView ThisLoc;
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
                    final EditText edittext = new EditText(getApplicationContext());
                    edittext.setHint("Название");
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
                    alert.setMessage("Введите информативное название");
                    alert.setTitle("Новая площадка");
                    alert.setView(edittext);
                    alert.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(edittext.getText().toString()!=null){
                                Place place = new Place(edittext.getText().toString(), latLng.longitude, latLng.latitude);
                                String ll = place.lat+","+place.lan;
                                String lli = ll.replace('.',',');
                                mDatabase.child("test/"+ lli).setValue(place);
                                Toast.makeText(getApplicationContext(), "Спасибо! Мы рассмотрим ваше предложение.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.show();
                }
            }
        });
    }
}
