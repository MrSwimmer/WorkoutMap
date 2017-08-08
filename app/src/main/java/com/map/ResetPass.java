package com.map;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPass extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    String emailAddress=null;
    EditText forgemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        forgemail = (EditText) findViewById(R.id.forgetmail);

    }
    public void resetPass(View view){
        if(forgemail.getText()!=null){
            emailAddress=forgemail.getText().toString();
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Письмо для сброса пароля отправлено вам на почту", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Произошла ошибка! Проверьте правильность данных и попробуйте снова", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(getApplicationContext(), "Заполните поле выше", Toast.LENGTH_SHORT).show();
        }
    }
}
