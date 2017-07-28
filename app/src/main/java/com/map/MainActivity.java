package com.map;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button but;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        but = (Button) findViewById(R.id.button);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
//                String geoUriString = "geo:0,0?q=где я&z=8";
//                Uri geoUri = Uri.parse(geoUriString);
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
//                startActivity(mapIntent);
            }
        });
    }
}
