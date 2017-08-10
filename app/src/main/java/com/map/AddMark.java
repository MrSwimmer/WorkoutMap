package com.map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMark extends AppCompatActivity {
    RatingBar newmark;
    Button save;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_mark);
        newmark = (RatingBar) findViewById(R.id.newrating);
        save = (Button) findViewById(R.id.savenewmark);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("realese/"+ ViewPlace.lli+"/rating").setValue((newmark.getRating()+ViewPlace.place.rating)/2);
                String Mail = ViewPlace.email.replace('.', ',');
                mDatabase.child("users/"+Mail+"/"+ViewPlace.lli).setValue(ViewPlace.lli);
                finish();
            }
        });
    }
}
