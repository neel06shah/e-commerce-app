package com.example.shreesaisugandhi;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView textView = findViewById(R.id.appname);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        a.reset();

        textView.startAnimation(a);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
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
