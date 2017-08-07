package com.map;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.R.attr.animation;

public class Main extends Activity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("realese");
    public static final ArrayList<Place> places = new ArrayList<Place>();
    LinearLayout Workout, Settings, Pools, Halls, Fitness, Train, Fight, Tennis, Foot, Basket, Volley, Velo;
    ObjectAnimator an0, an1, an2, an3, an4, an5, an6, an7, an8, an9, an10, an11;
    public static ObjectAnimator[] animations;
    public static LinearLayout[] layouts;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        count=0;
        Workout = (LinearLayout) findViewById(R.id.workout);
        Settings = (LinearLayout) findViewById(R.id.set);
        Pools = (LinearLayout) findViewById(R.id.pools);
        Halls = (LinearLayout) findViewById(R.id.halls);
        Fitness = (LinearLayout) findViewById(R.id.fitness);
        Train = (LinearLayout) findViewById(R.id.train);
        Fight = (LinearLayout) findViewById(R.id.fight);
        Tennis = (LinearLayout) findViewById(R.id.tennis);
        Foot = (LinearLayout) findViewById(R.id.foot);
        Basket = (LinearLayout) findViewById(R.id.bask);
        Volley = (LinearLayout) findViewById(R.id.volley);
        Velo = (LinearLayout) findViewById(R.id.velo);
        layouts = new LinearLayout[]{Train,  Settings, Pools, Workout, Halls, Fight, Tennis, Fitness, Foot, Volley, Basket, Velo};
        animations = new ObjectAnimator[]{an0, an1, an2, an3, an4, an5, an6, an7, an8, an9, an10, an11 };
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
    }
    private void rot(){
        animations[count] = ObjectAnimator.ofFloat(layouts[count], "rotationY", 270f, 360f);
        animations[count].setDuration(400);
        animations[count].setRepeatCount(ObjectAnimator.INFINITE);
        animations[count].setInterpolator(new AccelerateDecelerateInterpolator());
        animations[count].start();
        layouts[count].setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //animations[count].end();
                count++;
                if(count==12){
                    rotend();
                    return;
                }
                else{
                    rotend();
                    rot();
                }
            }
        }, 200);
    }
    private void rot2(){
        animations[count] = ObjectAnimator.ofFloat(layouts[count], "rotationY", 0.0f, 90f);
        animations[count].setDuration(400);
        animations[count].setRepeatCount(ObjectAnimator.INFINITE);
        animations[count].setInterpolator(new AccelerateDecelerateInterpolator());
        animations[count].start();
        layouts[count].setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //animations[count].end();
                count++;
                if(count==12){
                    rotend();
                    return;
                }
                else{
                    rotend();
                    rot2();
                }
            }
        }, 200);
    }
    private void rotend(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animations[count-1].end();
            }
        }, 200);
    }
    public void work(View view) {
        final Intent i = new Intent(Main.this, MapsActivity.class);
        count=0;
        rot2();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                overridePendingTransition(0,0);
            }
        }, 2700);
    }
    private void hand(String geo){
        Uri geoUri = Uri.parse(geo);
        final Intent map = new Intent(Intent.ACTION_VIEW, geoUri);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(map);
                overridePendingTransition(0,0);
            }
        }, 2700);
    }
    public void pools(View view){
        count=0;
        rot2();
        String geoUriString = "geo:0,0?q=бассейны рядом со мной&z=4";
        hand(geoUriString);
    }
    public void halls(View view){
        count=0;
        rot2();
        String geoUriString = "geo:0,0?q=манежи рядом со мной&z=4";
        hand(geoUriString);
    }
    public void fitness(View view){
        count=0;
        rot2();
        String geoUriString = "geo:0,0?q=фитнес рядом со мной&z=4";
        hand(geoUriString);
    }
    public void train(View view){
        count=0;
        rot2();
        String geoUriString = "geo:0,0?q=Тренажерные залы рядом со мной&z=4";
        hand(geoUriString);
    }
    public void fight(View view){
        count=0;
        rot2();
        String geoUriString = "geo:0,0?q=единоборства рядом со мной&z=4";
        hand(geoUriString);
    }
    public void tennis(View view){
        count=0;
        rot2();
        String geoUriString = "geo:0,0?q=теннис рядом со мной&z=4";
        hand(geoUriString);
    }
    public void foot(View view){
        count=0;
        rot2();
        String geoUriString = "geo:0,0?q=футболл рядом со мной&z=4";
        hand(geoUriString);
    }
    public void volley(View view){
        count=0;
        rot2();
        String geoUriString = "geo:0,0?q=воллейбол рядом со мной&z=4";
        hand(geoUriString);
    }
    public void bask(View view){
        count=0; rot2();
        String geoUriString = "geo:0,0?q=баскетбол рядом со мной&z=4";
        hand(geoUriString);
    }
    public void velo(View view){
        count=0; rot2();
        String geoUriString = "geo:0,0?q=велоспорт рядом со мной&z=4";
        hand(geoUriString);
    }
    public void settings(View view){
        final Intent i = new Intent(Main.this, Settings.class);
        count=0;
        rot2();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                overridePendingTransition(0,0);
            }
        }, 2700);
    }
    @Override
    protected void onResume() {
        count=0;
        rot();
        count=0;
        super.onResume();
    }
}
