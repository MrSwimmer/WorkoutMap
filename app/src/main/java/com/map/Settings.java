package com.map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Севастьян on 08.03.2017.
 */

public class Settings extends AppCompatActivity {
    private static final String DIALOG_DATE = "DialogDate";
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mMark;
    private TextView Email;
    private TextView mEnterRegButton;
    private String str = null;
    private TextView mVer;
    public static boolean SmartScore=true;
    private Switch mSwitch;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        mAuth = FirebaseAuth.getInstance();
        Email = (TextView) findViewById(R.id.email);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    Log.i("LOG", email);
                    mEnterRegButton.setText("Вход выполнен!");
                    Email.setText(email);
                    // User is signed in
                } else {
                    // User is signed out
                }
                // ...
            }
        };

        mEnterRegButton = (TextView) findViewById(R.id.EnterRegBut);
        mEnterRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this, LogInActivity.class);
                startActivity(i);
            }
        });
        mMark = (TextView) findViewById(R.id.buttonMark);
        mMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.map"));
                startActivity(intent);
            }
        });
        mVer = (TextView) findViewById(R.id.buttonVer);
        mVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setTitle("О версии").setMessage("Что нового в этой версии?\nЭто первая версия приложения!\n" +
                        "Что ожидать в следующих версиях?\nНовые фичи").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
