package com.example.shreesaisugandhi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Animation animation= AnimationUtils.loadAnimation(Splash.this,R.anim.fade_in);
                    //imgView.startAnimation(animation);
                    Thread.sleep(3000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                firebaseAuth = FirebaseAuth.getInstance();
                if(firebaseAuth.getCurrentUser() == null ) {
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(Splash.this, Dash.class);
                    startActivity(i);
                    finish();
                }
            }
        }).start();
    }
}
